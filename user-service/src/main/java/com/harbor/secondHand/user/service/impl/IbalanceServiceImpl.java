package com.harbor.secondHand.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.secondHand.user.domain.dto.RechargeDTO;
import com.harbor.secondHand.user.domain.dto.UserMessageDTO;
import com.harbor.secondHand.user.domain.po.RechargeRecord;
import com.harbor.secondHand.user.domain.po.User;
import com.harbor.secondHand.user.domain.po.UserBalance;
import com.harbor.secondHand.user.mapper.BalanceMapper;
import com.harbor.secondHand.user.mapper.UserMapper;
import com.harbor.secondHand.user.producer.UserMessageProducer;
import com.harbor.secondHand.user.service.IRechargeRecordService;
import com.harbor.secondHand.user.service.IbalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class IbalanceServiceImpl extends ServiceImpl<BalanceMapper, UserBalance> implements IbalanceService {

    private final UserMapper userMapper;

    private final UserMessageProducer userMessageProducer;

    private final IRechargeRecordService rechargeRecordService;

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
//        消息队列构建
        UserMessageDTO message = new UserMessageDTO();
        message.setUserId(user.getId());
        message.setUsername(user.getUsername());
        message.setAvatar(user.getImg());
        message.setPhone(user.getPhone());
        message.setEmail(user.getEmail());
        message.setCreditScore(user.getCreditScore());
        message.setUpdateTime(LocalDateTime.now());
        message.setBalance(userBalance.getBalance());
        userMessageProducer.sendUserMessage(message, "UPDATE");

        // 写入充值记录表
        String rechargeNo = UUID.randomUUID().toString();
        RechargeRecord rechargeRecord = new RechargeRecord().setUserId(rechargeDTO.getUserId())
            .setRechargeNo(rechargeNo)
            .setAmount(rechargeDTO.getAmount())
            .setStatus(1)
            .setBeforeBalance(0)
            .setAfterBalance(rechargeDTO.getAmount());
        rechargeRecordService.save(rechargeRecord);
    }
}
