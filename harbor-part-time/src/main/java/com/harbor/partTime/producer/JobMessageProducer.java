package com.harbor.partTime.producer;

import com.harbor.partTime.config.RabbitMQConfig;
import com.harbor.partTime.domain.po.PartTimePO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JobMessageProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  /**
   * 发送兼职信息变化消息
   * 
   * @param partTimePO    兼职信息
   * @param operationType 操作类型：CREATE, UPDATE, DELETE
   */
  public void sendJobMessage(PartTimePO partTimePO, String operationType) {
    try {
      Map<String, Object> message = new HashMap<>();
      message.put("jobId", partTimePO.getId());
      message.put("userId", partTimePO.getPublisherId());
      message.put("title", partTimePO.getTitle());
      message.put("description", partTimePO.getDescription());
      message.put("salary", partTimePO.getSalaryDesc());
      message.put("location", partTimePO.getLocation());
      message.put("status", partTimePO.getStatus());
      message.put("updateTime", LocalDateTime.now());
      message.put("operationType", operationType);

      rabbitTemplate.convertAndSend(
          RabbitMQConfig.EXCHANGE_NAME,
          RabbitMQConfig.JOB_ROUTING_KEY,
          message);
      log.info("发送兼职信息变化消息成功，jobId: {}, operationType: {}", partTimePO.getId(), operationType);
    } catch (Exception e) {
      log.error("发送兼职信息变化消息失败，jobId: {}, error: {}", partTimePO.getId(), e.getMessage(), e);
    }
  }
}
