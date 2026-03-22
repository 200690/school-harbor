package com.harbor.secondHand.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.secondHand.user.domain.dto.RechargeDTO;
import com.harbor.secondHand.user.domain.po.UserBalance;

import java.math.BigDecimal;

public interface IbalanceService extends IService<UserBalance> {

    void recharge(RechargeDTO rechargeDTO);

    void consume(BigDecimal payNo, Long userId);

    void refund(BigDecimal payNo, Long userId);

    void orderComplete(Long buyerId, BigDecimal payNo, Long sellerId);

    void refund(BigDecimal payNo, Long userId, Long sellId);
}
