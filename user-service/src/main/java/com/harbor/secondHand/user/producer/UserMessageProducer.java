package com.harbor.secondHand.user.producer;

import com.harbor.secondHand.user.config.RabbitMQConfig;
import com.harbor.secondHand.user.domain.dto.UserMessageDTO;
import com.harbor.secondHand.user.domain.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class UserMessageProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  /**
   * 发送用户信息变化消息
   *
   * @param message          用户信息
   * @param operationType 操作类型：CREATE, UPDATE, DELETE
   */
  public void sendUserMessage(UserMessageDTO message, String operationType) {
    try {
      message.setOperationType(operationType);
      rabbitTemplate.convertAndSend(
          RabbitMQConfig.EXCHANGE_NAME,
          RabbitMQConfig.USER_ROUTING_KEY,
          message);
      log.info("发送用户信息变化消息成功，userId: {}, operationType: {}", message.getUserId(), operationType);
    } catch (Exception e) {
      log.error("发送用户信息变化消息失败，userId: {}, error: {}", message.getUserId(), e.getMessage(), e);
    }
  }
}
