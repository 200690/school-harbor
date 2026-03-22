package com.harbor.secondHand.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.secondHand.domain.dto.CreateOrderDTO;
import com.harbor.secondHand.domain.po.ItemPO;
import com.harbor.secondHand.domain.po.OrderPO;
import com.harbor.secondHand.domain.vo.OrderListItemVO;
import com.harbor.secondHand.mapper.OrderMapper;
import com.harbor.secondHand.mapper.SecondHandMapper;
import com.harbor.secondHand.service.IOrder;
import com.harbor.utils.client.PayClient;
import com.harbor.utils.client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.harbor.common.utils.UserContext;
import com.harbor.secondHand.domain.dto.OrderMessageDTO;
import com.harbor.secondHand.producer.OrderMessageProducer;
import com.harbor.secondHand.producer.PublishNotificationProducer;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderImpl extends ServiceImpl<OrderMapper, OrderPO> implements IOrder {
    private final SecondHandMapper secondHandMapper;
    private final OrderMessageProducer orderMessageProducer;
    private final PublishNotificationProducer publishNotificationProducer;
    private final RedisTemplate<String, Object> redisTemplate;

    private final UserClient userClient;
    private final PayClient payClient;

    /**
     * 获取当前用户的订单列表
     *
     * @param pageQuery 分页参数
     * @return 订单列表
     */
    @Override
    public PageDTO<OrderListItemVO> getMyOrders(PageQuery pageQuery) {
        Long userId = UserContext.getUser();
        Assert.notNull(userId, "用户ID不能为空");
        Page<OrderPO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
//        构造条件
        LambdaQueryWrapper<OrderPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderPO::getBuyerId, userId);
        queryWrapper.eq(OrderPO::getIsDelete, 0);
        queryWrapper.orderByDesc(OrderPO::getOrderTime);

        Page<OrderPO> orderPOPage = this.page(page, queryWrapper);

        List<OrderListItemVO> listItemVOS = orderPOPage.getRecords().stream()
                .flatMap(orderPO -> {
                    ItemPO itemPO = secondHandMapper.selectById(orderPO.getItemId());
                    if (itemPO == null) {
                        // 商品不存在，跳过该订单
                        return Stream.empty();
                    }
                    OrderListItemVO orderListItemVO = new OrderListItemVO();
                    BeanUtil.copyProperties(orderPO, orderListItemVO, CopyOptions.create().ignoreNullValue());
                    orderListItemVO.setItemTitle(itemPO.getTitle());
                    orderListItemVO.setItemCoverImage(itemPO.getCoverImage());
                    return Stream.of(orderListItemVO);
                })
                .toList();
        return new PageDTO<>(orderPOPage.getTotal(), orderPOPage.getPages(), listItemVOS);
    }

    /**
     * 确认收货
     *
     * @param id 订单ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmReceipt(Long id) {
        // 修改订单状态
        Assert.notNull(id, "订单ID不能为空");
        OrderPO orderPO = this.getById(id);
        Assert.notNull(orderPO, "订单不存在");
        Assert.isTrue(orderPO.getStatus() == 2, "订单状态错误");
        orderPO.setStatus(0);
        this.updateById(orderPO);
        // 修改商品状态
        ItemPO itemPO = secondHandMapper.selectById(orderPO.getItemId());
        itemPO.setStatus(3);
        secondHandMapper.updateById(itemPO);
        // 消息队列发送给商家交易完成
        sendOrderCompleteNotification(orderPO, itemPO);

        //pay微服务修改订单状态
        payClient.updateOrderStatus(orderPO.getId(), 0);
        //user微服务扣减和增加冻结资金
        userClient.orderComplete(orderPO.getBuyerId(), orderPO.getTotalAmount(), orderPO.getSellerId());
    }

    /**
     * 取消订单
     *
     * @param id 订单ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long id) {
        Assert.notNull(id, "订单ID不能为空");
        OrderPO orderPO = this.getById(id);
        Assert.notNull(orderPO, "订单不存在");
        Assert.isTrue(orderPO.getStatus() == 2, "订单状态错误");
        orderPO.setStatus(4);
        this.updateById(orderPO);
        // 修改商品状态
        ItemPO itemPO = secondHandMapper.selectById(orderPO.getItemId());
        itemPO.setStatus(1);
        secondHandMapper.updateById(itemPO);
        // 消息队列通知卖家订单取消
        sendOrderCancelNotification(orderPO, itemPO);

        //pay微服务修改订单状态
        payClient.updateOrderStatus(orderPO.getId(), 4);
        userClient.refund(orderPO.getTotalAmount(), orderPO.getBuyerId());
    }

    /**
     * 创建订单
     *
     * @param createOrderDTO 创建订单参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(CreateOrderDTO createOrderDTO) {
        // 生成订单号
        String orderNo = generateOrderNo();
        
        // 扣款
        userClient.consume(createOrderDTO.getTotalAmount(), UserContext.getUser());
        
        // 创建订单对象
        OrderPO orderPO = new OrderPO();
        orderPO.setOrderNo(orderNo);
        orderPO.setItemId(createOrderDTO.getItemId());
        orderPO.setSellerId(createOrderDTO.getSellerId());
        orderPO.setBuyerId(UserContext.getUser());
        orderPO.setPrice(createOrderDTO.getPrice());
        orderPO.setTotalAmount(createOrderDTO.getTotalAmount());
        orderPO.setStatus(2); // 待发货状态
        orderPO.setPayStatus(1); // 支付成功状态
        orderPO.setQuantity(1); // 默认购买数量为1

        // 保存订单到数据库
        this.save(orderPO);

        // 创建订单消息DTO
        OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
        orderMessageDTO.setOrderId(orderPO.getId());
        orderMessageDTO.setOrderNo(orderPO.getOrderNo());
        orderMessageDTO.setItemId(orderPO.getItemId());
        orderMessageDTO.setSellerId(orderPO.getSellerId());
        orderMessageDTO.setBuyerId(orderPO.getBuyerId());
        orderMessageDTO.setPrice(orderPO.getPrice());
        orderMessageDTO.setTotalAmount(orderPO.getTotalAmount());
        
        // 发送订单创建消息到消息队列
        orderMessageProducer.sendOrderMessage(orderMessageDTO, "CREATE");

        // 修改商品状态为已售出
        ItemPO itemPO = secondHandMapper.selectById(createOrderDTO.getItemId());
        if (itemPO != null) {
            itemPO.setStatus(2); // 2-已售出
            secondHandMapper.updateById(itemPO);
            log.info("商品状态已修改为已售出，itemId: {}", createOrderDTO.getItemId());
        }

        // 清除二手商品Redis缓存
        clearItemCache(createOrderDTO.getItemId());

        // 发送订单创建成功通知
        sendOrderCreateNotification(orderPO, itemPO);

    }

    /**
     * 取消商品
     *
     * @param itemId 商品ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelItem(Long itemId) {
        // 根据商品ID获取最新的下单时间的订单
        LambdaQueryWrapper<OrderPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderPO::getItemId, itemId);
        queryWrapper.eq(OrderPO::getStatus, 2); // 只处理待发货状态的订单
        queryWrapper.orderByDesc(OrderPO::getOrderTime);
        OrderPO orderPO = this.getOne(queryWrapper);
        
        Assert.notNull(orderPO, "未找到待发货状态的订单");
        
        // 取消订单
        orderPO.setStatus(4);
        orderPO.setCancelTime(LocalDateTime.now());
        orderPO.setCancelReason("卖家取消");
        this.updateById(orderPO);
        
        // 修改商品状态为在售
        ItemPO itemPO = secondHandMapper.selectById(itemId);
        itemPO.setStatus(1);
        secondHandMapper.updateById(itemPO);
        
        // 发送订单取消通知
        sendOrderCancelNotification(orderPO, itemPO);
        
        //pay微服务修改订单状态
        payClient.updateOrderStatus(orderPO.getId(), 4);
        //user微服务退款
        userClient.refund(orderPO.getTotalAmount(), orderPO.getBuyerId());
    }

    /**
     * 退款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refund(Long itemId) {
        // 根据商品ID获取最新的下单时间的订单
        LambdaQueryWrapper<OrderPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderPO::getItemId, itemId);
        queryWrapper.in(OrderPO::getStatus, 2,3,0);
        queryWrapper.orderByDesc(OrderPO::getOrderTime);
        OrderPO orderPO = this.getOne(queryWrapper);
        
        Assert.notNull(orderPO, "未找到订单");

        // 退款处理
        if(orderPO.getStatus() == 2 || orderPO.getStatus() == 3){
            orderPO.setStatus(5); // 5-退款状态
            orderPO.setCancelTime(LocalDateTime.now());
            orderPO.setCancelReason("商家退款");
            this.updateById(orderPO);
            //user微服务退款
            userClient.refund(orderPO.getTotalAmount(), orderPO.getBuyerId());
        } else{
            userClient.refund(orderPO.getTotalAmount(), orderPO.getBuyerId(), orderPO.getSellerId());
        }
        
        //pay微服务修改订单状态
        payClient.updateOrderStatus(orderPO.getId(), 5);

    }

    /**
     * 发送订单创建成功通知
     *
     * @param orderPO 订单信息
     * @param itemPO 商品信息
     */
    private void sendOrderCreateNotification(OrderPO orderPO, ItemPO itemPO) {
        if (itemPO != null) {
            // 为买家发送通知
            publishNotificationProducer.sendOrderCreateNotificationForBuyer(
                    orderPO.getId(),
                    orderPO.getBuyerId(),
                    String.format("订单已创建成功，商品：%s，订单号：%s", itemPO.getTitle(), orderPO.getOrderNo())
            );

            // 为卖家发送通知
            publishNotificationProducer.sendOrderCreateNotificationForSeller(
                    orderPO.getId(),
                    orderPO.getSellerId(),
                    String.format("您的商品 %s 已被购买，订单号：%s", itemPO.getTitle(), orderPO.getOrderNo())
            );
        }
    }

    /**
     * 发送订单完成通知
     *
     * @param orderPO 订单信息
     * @param itemPO 商品信息
     */
    private void sendOrderCompleteNotification(OrderPO orderPO, ItemPO itemPO) {
        if (itemPO != null) {
            // 发送订单状态变更消息到消息队列
            OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
            orderMessageDTO.setOrderId(orderPO.getId());
            orderMessageDTO.setOrderNo(orderPO.getOrderNo());
            orderMessageDTO.setItemId(orderPO.getItemId());
            orderMessageDTO.setSellerId(orderPO.getSellerId());
            orderMessageDTO.setBuyerId(orderPO.getBuyerId());
            orderMessageDTO.setPrice(orderPO.getPrice());
            orderMessageDTO.setTotalAmount(orderPO.getTotalAmount());
            orderMessageProducer.sendOrderMessage(orderMessageDTO, "UPDATE");

            // 为卖家发送通知
            publishNotificationProducer.sendOrderCreateNotificationForSeller(
                    orderPO.getId(),
                    orderPO.getSellerId(),
                    String.format("交易完成！您的商品 %s 已被确认收货，订单号：%s", itemPO.getTitle(), orderPO.getOrderNo())
            );

            // 为买家发送通知
            publishNotificationProducer.sendOrderCreateNotificationForBuyer(
                    orderPO.getId(),
                    orderPO.getBuyerId(),
                    String.format("交易完成！您购买的商品 %s 已确认收货，订单号：%s", itemPO.getTitle(), orderPO.getOrderNo())
            );
        }
    }

    /**
     * 发送订单取消通知
     *
     * @param orderPO 订单信息
     * @param itemPO 商品信息
     */
    private void sendOrderCancelNotification(OrderPO orderPO, ItemPO itemPO) {
        if (itemPO != null) {
            // 发送订单状态变更消息到消息队列
            OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
            orderMessageDTO.setOrderId(orderPO.getId());
            orderMessageDTO.setOrderNo(orderPO.getOrderNo());
            orderMessageDTO.setItemId(orderPO.getItemId());
            orderMessageDTO.setSellerId(orderPO.getSellerId());
            orderMessageDTO.setBuyerId(orderPO.getBuyerId());
            orderMessageDTO.setPrice(orderPO.getPrice());
            orderMessageDTO.setTotalAmount(orderPO.getTotalAmount());
            orderMessageProducer.sendOrderMessage(orderMessageDTO, "CANCEL");

            // 为卖家发送通知
            publishNotificationProducer.sendOrderCreateNotificationForSeller(
                    orderPO.getId(),
                    orderPO.getSellerId(),
                    String.format("订单已取消！您的商品 %s 订单已取消，订单号：%s", itemPO.getTitle(), orderPO.getOrderNo())
            );

            // 为买家发送通知
            publishNotificationProducer.sendOrderCreateNotificationForBuyer(
                    orderPO.getId(),
                    orderPO.getBuyerId(),
                    String.format("订单已取消！您购买的商品 %s 订单已取消，订单号：%s", itemPO.getTitle(), orderPO.getOrderNo())
            );
        }
    }

    /**
     * 清除二手商品相关缓存
     *
     * @param itemId 商品ID
     */
    private void clearItemCache(Long itemId) {
        try {
            // 清除商品详情缓存
            String detailCacheKey = "item:detail:" + itemId;
            redisTemplate.delete(detailCacheKey);
            log.info("清除商品详情缓存: {}", detailCacheKey);

            // 清除个人中心缓存
            String userCacheKey = "user:center:" + UserContext.getUser();
            redisTemplate.delete(userCacheKey);
            log.info("清除商品详情缓存: {}", userCacheKey);

            // 清除商品列表缓存（匹配所有用户的列表缓存）
            String listCachePattern = "item:list:latest:*";
            var keys = redisTemplate.keys(listCachePattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("清除商品列表缓存，数量: {}", keys.size());
            }

            // 清除推荐列表缓存
            String recommendCacheKey = "item:list:recommend";
            redisTemplate.delete(recommendCacheKey);
            log.info("清除商品推荐列表缓存: {}", recommendCacheKey);

        } catch (Exception e) {
            log.error("清除商品缓存失败，itemId: {}, error: {}", itemId, e.getMessage());
        }
    }
    
    /**
     * 生成订单号
     * 格式：ORDER + 年月日时分秒 + 6位随机数
     */
    private String generateOrderNo() {
        StringBuilder sb = new StringBuilder();
        sb.append("ORDER");
        sb.append(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        sb.append(String.format("%06d", (int) (Math.random() * 1000000)));
        return sb.toString();
    }
}
