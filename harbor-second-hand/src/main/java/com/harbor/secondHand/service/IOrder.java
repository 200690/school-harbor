package com.harbor.secondHand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.common.domain.PageDTO;
import com.harbor.common.domain.PageQuery;
import com.harbor.secondHand.domain.dto.CreateOrderDTO;
import com.harbor.secondHand.domain.po.OrderPO;
import com.harbor.secondHand.domain.vo.OrderListItemVO;

public interface IOrder extends IService<OrderPO> {
    PageDTO<OrderListItemVO> getMyOrders(PageQuery pageQuery);

    void confirmReceipt(Long ItemId);

    void cancelOrder(Long ItemId);

    void createOrder(CreateOrderDTO createOrderDTO);
}
