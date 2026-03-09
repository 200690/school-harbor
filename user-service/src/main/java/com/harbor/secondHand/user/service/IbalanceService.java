package com.harbor.secondHand.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.secondHand.user.domain.dto.RechargeDTO;
import com.harbor.secondHand.user.domain.po.UserBalance;

import java.math.BigDecimal;

public interface IbalanceService extends IService<UserBalance> {

    void recharge(RechargeDTO rechargeDTO);

    void consume(BigDecimal payNo);
}
