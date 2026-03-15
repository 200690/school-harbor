package com.harbor.secondHand.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.harbor.utils.client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.harbor.common.utils.UserContext;
import com.harbor.secondHand.domain.dto.OrderMessageDTO;
import com.harbor.secondHand.producer.OrderMessageProducer;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderImpl extends ServiceImpl<OrderMapper, OrderPO> implements IOrder {
    private final SecondHandMapper secondHandMapper;
    private final UserClient userClient;
    private final OrderMessageProducer orderMessageProducer;
    private final RedisTemplate<String, Object> redisTemplate;

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

        List<OrderListItemVO> listItemVOS = orderPOPage.getRecords().stream().map(orderPO -> {
            OrderListItemVO orderListItemVO = new OrderListItemVO();
            BeanUtil.copyProperties(orderPO, orderListItemVO, CopyOptions.create().ignoreNullValue());
            ItemPO itemPO = secondHandMapper.selectById(orderPO.getItemId());
            orderListItemVO.setItemTitle(itemPO.getTitle());
            orderListItemVO.setItemCoverImage(itemPO.getCoverImage());
            return orderListItemVO;
        }).toList();
        return new PageDTO<>(orderPOPage.getTotal(), orderPOPage.getPages(), listItemVOS);
    }

    /**
     * 确认收货
     *
     * @param ItemId 订单ID
     */
    @Override
    public void confirmReceipt(Long ItemId) {
        Assert.notNull(ItemId, "订单ID不能为空");
        OrderPO orderPO = this.getById(ItemId);
        Assert.notNull(orderPO, "订单不存在");
        Assert.isTrue(orderPO.getStatus() == 3, "订单状态错误");
        orderPO.setStatus(4);
        //消息队列发送TODO
    }

    @Override
    public void cancelOrder(Long ItemId) {
        Assert.notNull(ItemId, "订单ID不能为空");
        OrderPO orderPO = lambdaQuery().eq(OrderPO::getItemId, ItemId).one();
        Assert.notNull(orderPO, "订单不存在");
        Assert.isTrue(orderPO.getStatus() == 1 || orderPO.getStatus() == 2, "订单状态错误");
        if(orderPO.getStatus() == 1){
            orderPO.setStatus(4);
            this.updateById(orderPO);
        }else {
            orderPO.setStatus(5);
            this.updateById(orderPO);
            //TODO 消息队列退款
        }
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
