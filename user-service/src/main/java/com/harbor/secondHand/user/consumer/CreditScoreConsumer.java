package com.harbor.secondHand.user.consumer;

import com.harbor.secondHand.user.domain.po.User;
import com.harbor.secondHand.user.mapper.UserMapper;
import com.harbor.utils.dto.CreditScoreChangeDTO;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreditScoreConsumer {
    
    private final UserMapper userMapper;
    
    @RabbitListener(queues = "credit.score.change.queue")
    @Transactional(rollbackFor = Exception.class)
    public void handleCreditScoreChange(CreditScoreChangeDTO messageDTO, Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("收到信誉分变动消息: userId={}, changeValue={}", messageDTO.getUserId(), messageDTO.getChangeValue());
            
            // 获取用户信息
            User user = userMapper.selectById(messageDTO.getUserId());
            if (user == null) {
                log.error("用户不存在: userId={}", messageDTO.getUserId());
                // 用户不存在，确认消息，避免重复消费
                channel.basicAck(deliveryTag, false);
                return;
            }
            
            // 计算新的信誉分
            Integer currentCreditScore = user.getCreditScore() != null ? user.getCreditScore() : 0;
            Integer newCreditScore = currentCreditScore + messageDTO.getChangeValue();
            
            // 更新用户信誉分
            user.setCreditScore(newCreditScore);
            
            // 如果信誉分低于30，设置用户状态为2
            if (newCreditScore < 30) {
                user.setStatus(2);
                log.info("用户信誉分低于30，设置状态为2: userId={}, creditScore={}", messageDTO.getUserId(), newCreditScore);
            }
            
            // 保存更新
            userMapper.updateById(user);
            log.info("用户信誉分更新成功: userId={}, oldScore={}, newScore={}, status={}", 
                    messageDTO.getUserId(), currentCreditScore, newCreditScore, user.getStatus());
            
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
            
        } catch (Exception e) {
            log.error("处理信誉分变动消息失败: userId={}, error={}", messageDTO.getUserId(), e.getMessage(), e);
            // 处理失败，拒绝消息并重新入队
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ioException) {
                log.error("消息拒绝失败: {}", ioException.getMessage(), ioException);
            }
        }
    }
}
