package com.harbor.messageservice.consumer;

import com.harbor.messageservice.config.RabbitMQConfig;
import com.harbor.messageservice.domain.message.UserMessage;
import com.harbor.messageservice.domain.po.UserProfilePO;
import com.harbor.messageservice.domain.po.UserStatisticsPO;
import com.harbor.messageservice.mapper.UserMapper;
import com.harbor.messageservice.mapper.UserStatisticsMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final UserMapper userMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    private final UserStatisticsMapper userStatisticsMapper;

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE_NAME)
    public void handleUserMessage(UserMessage userMessage, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("接收到用户信息变化消息: {}", userMessage);

            // 处理消息
            processUserMessage(userMessage);

            // 确认消息
            channel.basicAck(deliveryTag, false);
            log.info("用户信息变化消息处理成功，userId: {}", userMessage.getUserId());
        } catch (Exception e) {
            log.error("处理用户信息变化消息失败: {}", e.getMessage(), e);
            // 拒绝消息，重新入队
            channel.basicNack(deliveryTag, false, true);
        }
    }

    private void processUserMessage(UserMessage userMessage) {
        switch (userMessage.getOperationType()) {
            case "CREATE":
            case "UPDATE":
                // 查找是否已存在
                UserProfilePO existingUser = userMapper.selectByUserId(userMessage.getUserId());
                if (existingUser != null) {
                    // 更新
                    existingUser.setUsername(userMessage.getUsername());
                    existingUser.setAvatar(userMessage.getAvatar());
                    existingUser.setPhone(userMessage.getPhone());
                    existingUser.setEmail(userMessage.getEmail());
                    existingUser.setBalance(userMessage.getBalance());
                    existingUser.setCreditScore(userMessage.getCreditScore());
                    existingUser.setUpdateTime(userMessage.getUpdateTime());
                    userMapper.updateById(existingUser);
                    log.info("更新用户信息: userId={}", userMessage.getUserId());
//                    清除缓存
                    log.info("清除用户信息缓存: userId={}", userMessage.getUserId());
                    String cacheKey = "user:center:" + userMessage.getUserId();
                    redisTemplate.delete(cacheKey);

                } else {
                    // 创建
                    UserProfilePO newUser = new UserProfilePO();
                    newUser.setUserId(userMessage.getUserId());
                    newUser.setUsername(userMessage.getUsername());
                    newUser.setAvatar(userMessage.getAvatar());
                    newUser.setPhone(userMessage.getPhone());
                    newUser.setEmail(userMessage.getEmail());
                    newUser.setBalance(userMessage.getBalance());
                    newUser.setCreditScore(userMessage.getCreditScore());
                    userMapper.insert(newUser);
                    log.info("创建用户信息: userId={}", userMessage.getUserId());
//                    写入缓存
                    String cacheKey = "user:center:" + userMessage.getUserId();
                    redisTemplate.opsForValue().set(cacheKey, newUser, 1, java.util.concurrent.TimeUnit.HOURS);
//                    创建用户统计数据
                    UserStatisticsPO userStatisticsPO = new UserStatisticsPO()
                            .setUserId(userMessage.getUserId())
                            .setItemApplyCount(0)
                            .setItemPostCount(0)
                            .setItemFavoriteCount(0)
                            .setItemPurchaseCount(0)
                            .setJobApplyCount(0)
                            .setJobFavoriteCount(0)
                            .setJobPostCount(0);
                    userStatisticsMapper.insert(userStatisticsPO);
                }
                break;
            case "DELETE":
                // 删除
                userMapper.deleteByUserId(userMessage.getUserId());
                log.info("删除用户信息: userId={}", userMessage.getUserId());
                break;
            default:
                log.warn("未知的操作类型: {}", userMessage.getOperationType());
        }
    }
}
