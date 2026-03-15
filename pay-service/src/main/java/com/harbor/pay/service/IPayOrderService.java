package com.harbor.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.pay.domain.dto.CreateDTO;
import com.harbor.pay.domain.dto.OrderMessageDTO;
import com.harbor.pay.domain.po.PayOrderPo;

public interface IPayOrderService extends IService<PayOrderPo> {

    /**
     * 根据订单消息创建支付订单
     *
     * @param orderMessageDTO 订单消息
     */
    void createPayOrderFromMessage(OrderMessageDTO orderMessageDTO);
}
