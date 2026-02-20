package com.harbor.secondHand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.domain.po.OrderPO;
import com.harbor.secondHand.mapper.Order;
import com.harbor.secondHand.service.IOrder;
import org.springframework.stereotype.Service;

@Service
public class OrderImpl extends ServiceImpl<Order, OrderPO> implements IOrder {
}
