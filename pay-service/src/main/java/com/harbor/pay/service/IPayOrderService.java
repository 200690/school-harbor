package com.harbor.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.pay.domain.dto.CreateDTO;
import com.harbor.pay.domain.po.PayOrderPo;

public interface IPayOrderService extends IService<PayOrderPo> {
    void createPayOrder(CreateDTO createDTO);
}
