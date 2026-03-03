package com.harbor.secondHand.user.producer;

import com.harbor.secondHand.user.config.RabbitMQConfig;
import com.harbor.secondHand.user.domain.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserMessageProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  /**
   * 发送用户信息变化消息
   * 
   * @param user          用户信息
   * @param operationType 操作类型：CREATE, UPDATE, DELETE
   */
  public void sendUserMessage(User user, String operationType) {
    try {
      Map<String, Object> message = new HashMap<>();
      message.put("userId", user.getId());
      message.put("username", user.getUsername());
      message.put("avatar", user.getImg());
      message.put("phone", user.getPhone());
      message.put("email", user.getEmail());
      message.put("creditScore", user.getCreditScore());
      message.put("updateTime", LocalDateTime.now());
      message.put("operationType", operationType);

      rabbitTemplate.convertAndSend(
          RabbitMQConfig.EXCHANGE_NAME,
          RabbitMQConfig.USER_ROUTING_KEY,
          message);
      log.info("发送用户信息变化消息成功，userId: {}, operationType: {}", user.getId(), operationType);
    } catch (Exception e) {
      log.error("发送用户信息变化消息失败，userId: {}, error: {}", user.getId(), e.getMessage(), e);
    }
  }
}
