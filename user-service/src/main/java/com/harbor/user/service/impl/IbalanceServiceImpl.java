package com.harbor.user.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.user.domain.dto.RechargeDTO;
import com.harbor.user.domain.po.RechargeRecord;
import com.harbor.user.domain.po.User;
import com.harbor.user.mapper.UserMapper;
import com.harbor.user.service.IUserService;
import com.harbor.user.service.IbalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class IbalanceServiceImpl extends ServiceImpl<BalanceMapper, RechargeRecord> implements IbalanceService {
    private final IUserService userService;
    /**
     * 用户充值
     *
     * @param rechargeDTO 充值参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recharge(RechargeDTO rechargeDTO) {
        User user = lambdaQuery().eq(User::getId, rechargeDTO.getUserId()).one();
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(Objects.equals(user.getStatus(), 1), "用户状态异常");
        String rechargeNo = "RC" + System.currentTimeMillis() +
                RandomStringUtils.randomNumeric(6);
        RechargeRecord rechargeRecord = new RechargeRecord()
                .setUserId(user.getId())
                .setRechargeNo(rechargeNo)
                .setAmount(rechargeDTO.getAmount())
                .setStatus(1)
                .setBeforeBalance(user.getBalance())
                .setAfterBalance(user.getBalance() + rechargeDTO.getAmount())
                .setRechargeTime(LocalDateTime.now())
                .setCreateTime(LocalDateTime.now());
        this.save(rechargeRecord);
        lambdaQuery().eq()
        user.setBalance(user.getBalance() + rechargeDTO.getAmount());
        user.setUpdateTime(LocalDateTime.now());
        this.updateById(user);
    }
}
