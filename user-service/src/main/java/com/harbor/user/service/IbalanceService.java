package com.harbor.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbor.user.domain.dto.RechargeDTO;
import com.harbor.user.domain.po.RechargeRecord;
import com.harbor.user.domain.po.User;

public interface IbalanceService extends IService<RechargeRecord> {

    void recharge(RechargeDTO rechargeDTO);

}
