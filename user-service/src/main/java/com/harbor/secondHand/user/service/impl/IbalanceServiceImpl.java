package com.harbor.secondHand.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.user.domain.dto.RechargeDTO;
import com.harbor.secondHand.user.domain.po.User;
import com.harbor.secondHand.user.domain.po.UserBalance;
import com.harbor.secondHand.user.mapper.BalanceMapper;
import com.harbor.secondHand.user.mapper.UserMapper;
import com.harbor.secondHand.user.service.IbalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class IbalanceServiceImpl extends ServiceImpl<BalanceMapper, UserBalance> implements IbalanceService {

    private final UserMapper userMapper;

    /**
     * 用户充值
     *
     * @param rechargeDTO 充值参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recharge(RechargeDTO rechargeDTO) {
//        判断用户状态
        User user = userMapper.selectById(rechargeDTO.getUserId());
        log.info("开始充值,user：{}", user);
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(Objects.equals(user.getStatus(), 1), "用户状态异常");
//        准备写入数据库
        UserBalance userBalance = lambdaQuery().eq(UserBalance::getUserId, rechargeDTO.getUserId()).one();
        if (Objects.isNull(userBalance)) {
            userBalance = new UserBalance()
                    .setUserId(user.getId())
                    .setBalance(rechargeDTO.getAmount())
                    .setTotalRecharge(BigDecimal.valueOf(rechargeDTO.getAmount()))
                    .setCreateTime(LocalDateTime.now())
                    .setUpdateTime(LocalDateTime.now());
            this.save(userBalance);
        }else{
            userBalance.setBalance(userBalance.getBalance() + rechargeDTO.getAmount())
                    .setUpdateTime(LocalDateTime.now())
                    .setTotalRecharge(userBalance.getTotalRecharge().add(BigDecimal.valueOf(rechargeDTO.getAmount())));
            this.updateById(userBalance);
        }
        log.info("用户充值成功：{}", userBalance);
    }
}
