package com.harbor.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.pay.domain.po.PayOrderPo;
import com.harbor.pay.mapper.PayOrderMapper;
import com.harbor.pay.service.IPayOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayOrderImpl extends ServiceImpl<PayOrderMapper, PayOrderPo> implements IPayOrderService {
}
