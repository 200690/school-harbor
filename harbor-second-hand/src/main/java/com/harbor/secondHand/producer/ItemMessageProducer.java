package com.harbor.secondHand.producer;

import com.harbor.secondHand.config.RabbitMQConfig;
import com.harbor.secondHand.domain.po.ItemPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ItemMessageProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  /**
   * 发送二手交易信息变化消息
   * 
   * @param itemPO        二手交易信息
   * @param operationType 操作类型：CREATE, UPDATE, DELETE
   */
  public void sendItemMessage(ItemPO itemPO, String operationType) {
    try {
      Map<String, Object> message = new HashMap<>();
      message.put("itemId", itemPO.getId());
      message.put("userId", itemPO.getSellerId());
      message.put("title", itemPO.getTitle());
      message.put("description", itemPO.getDescription());
      message.put("price", itemPO.getPrice());
      message.put("status", itemPO.getStatus());
      message.put("updateTime", LocalDateTime.now());
      message.put("operationType", operationType);

      rabbitTemplate.convertAndSend(
          RabbitMQConfig.EXCHANGE_NAME,
          RabbitMQConfig.ITEM_ROUTING_KEY,
          message);
      log.info("发送二手交易信息变化消息成功，itemId: {}, operationType: {}", itemPO.getId(), operationType);
    } catch (Exception e) {
      log.error("发送二手交易信息变化消息失败，itemId: {}, error: {}", itemPO.getId(), e.getMessage(), e);
    }
  }
}
