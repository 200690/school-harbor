package com.harbor.secondHand.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbor.common.utils.UserContext;
import com.harbor.secondHand.user.domain.dto.RechargeDTO;
import com.harbor.secondHand.user.domain.dto.UserMessageDTO;
import com.harbor.secondHand.user.domain.po.User;
import com.harbor.secondHand.user.domain.po.UserBalance;
import com.harbor.secondHand.user.mapper.BalanceMapper;
import com.harbor.secondHand.user.mapper.UserMapper;
import com.harbor.secondHand.user.producer.UserMessageProducer;
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

    private final UserMessageProducer userMessageProducer;

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
                    .setTotalRecharge(rechargeDTO.getAmount())
                    .setCreateTime(LocalDateTime.now())
                    .setUpdateTime(LocalDateTime.now());
            this.save(userBalance);
        }else{
            userBalance.setBalance(userBalance.getBalance().add(rechargeDTO.getAmount()))
                    .setUpdateTime(LocalDateTime.now())
                    .setTotalRecharge(userBalance.getTotalRecharge().add(rechargeDTO.getAmount()));
            this.updateById(userBalance);
        }
        log.info("用户充值成功：{}", userBalance);
        UserMessageDTO messageDTO = UserMessageDTO.userToUserMessageDTO(user);
        messageDTO.setBalance(userBalance.getBalance());
        userMessageProducer.sendUserMessage(messageDTO, "UPDATE");
    }

    // 消费
    @Override
    public void consume(BigDecimal payNo, Long userId) {
        UserBalance one = lambdaQuery().eq(UserBalance::getUserId, userId).one();
        Assert.notNull(one, "用户不存在");
        Assert.isTrue(one.getBalance().compareTo(payNo) >= 0, "余额不足");
//        更新余额和冻结金额
        one.setBalance(one.getBalance().subtract(payNo))
                .setUpdateTime(LocalDateTime.now())
                .setFrozenBalance(one.getFrozenBalance().add(payNo));
        this.updateById(one);
        this.sendUserMessage(userId, one);
    }

    // 退款
    @Override
    public void refund(BigDecimal payNo, Long userId) {
        Assert.notNull(payNo, "支付金额不能为空");
        Assert.notNull(userId, "用户id不能为空");
        UserBalance one = lambdaQuery().eq(UserBalance::getUserId, userId).one();
        Assert.notNull(one, "用户不存在或已被封禁");
        Assert.isTrue(one.getFrozenBalance().compareTo(payNo) >= 0, "冻结金额不足");
        one.setBalance(one.getBalance().add(payNo))
                .setUpdateTime(LocalDateTime.now())
                .setFrozenBalance(one.getFrozenBalance().subtract(payNo));
        this.updateById(one);
        this.sendUserMessage(userId, one);
    }

    // 订单完成
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderComplete(Long buyerId, BigDecimal payNo, Long sellerId) {
        UserBalance buyerBalance = this.getById(buyerId);
        UserBalance sellerBalance = this.getById(sellerId);
        Assert.notNull(buyerBalance, "买家不存在");
        Assert.notNull(sellerBalance, "卖家不存在");
        buyerBalance.setBalance(buyerBalance.getBalance())
                .setUpdateTime(LocalDateTime.now())
                .setTotalConsume(buyerBalance.getTotalConsume().add(payNo))
                .setFrozenBalance(buyerBalance.getFrozenBalance().subtract(payNo));
        sellerBalance.setBalance(sellerBalance.getBalance().add(payNo))
                .setUpdateTime(LocalDateTime.now())
                .setTotalRecharge(sellerBalance.getTotalRecharge().add(payNo));
        this.updateById(buyerBalance);
        this.updateById(sellerBalance);
        this.sendUserMessage(buyerId, buyerBalance);
        this.sendUserMessage(sellerId, sellerBalance);
    }

    @Override
    public void refund(BigDecimal payNo, Long userId, Long sellId) {
        Assert.notNull(payNo, "支付金额不能为空");
        Assert.notNull(userId, "用户id不能为空");
        UserBalance one = lambdaQuery().eq(UserBalance::getUserId, userId).one();
        Assert.notNull(one, "用户不存在或已被封禁");
        one.setBalance(one.getBalance().add(payNo))
                .setUpdateTime(LocalDateTime.now());
        // 商家扣款
        UserBalance sellerBalance = lambdaQuery().eq(UserBalance::getUserId, sellId).one();
        sellerBalance.setBalance(sellerBalance.getBalance().subtract(payNo))
                .setUpdateTime(LocalDateTime.now());
        this.updateById(sellerBalance);
        this.updateById(one);
        this.sendUserMessage(userId, one);
        this.sendUserMessage(sellId, sellerBalance);
    }

    // 发送消息通知消息微服务更新用户余额
    private void sendUserMessage(Long userId, UserBalance one) {
        User user = userMapper.selectById(userId);
        UserMessageDTO messageDTO = UserMessageDTO.userToUserMessageDTO(user);
        messageDTO.setBalance(one.getBalance());
        userMessageProducer.sendUserMessage(messageDTO, "UPDATE");
    }
}
