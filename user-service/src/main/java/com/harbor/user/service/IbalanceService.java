package com.harbor.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.user.domain.dto.RechargeDTO;
import com.harbor.user.domain.po.UserBalance;

public interface IbalanceService extends IService<UserBalance> {

    void recharge(RechargeDTO rechargeDTO);

}
