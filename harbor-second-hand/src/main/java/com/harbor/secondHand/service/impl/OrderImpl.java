package com.harbor.secondHand.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.secondHand.domain.po.ItemPO;
import com.harbor.secondHand.domain.po.OrderPO;
import com.harbor.secondHand.domain.vo.OrderListItemVO;
import com.harbor.secondHand.mapper.OrderMapper;
import com.harbor.secondHand.mapper.SecondHandMapper;
import com.harbor.secondHand.service.IOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderImpl extends ServiceImpl<OrderMapper, OrderPO> implements IOrder {
    private final SecondHandMapper secondHandMapper;

    @Override
    public PageDTO<OrderListItemVO> getMyOrders(PageQuery pageQuery) {
        Assert.notNull(pageQuery.getId(), "用户ID不能为空");
        Page<OrderPO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
//        构造条件
        LambdaQueryWrapper<OrderPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderPO::getBuyerId, pageQuery.getId());
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
}
