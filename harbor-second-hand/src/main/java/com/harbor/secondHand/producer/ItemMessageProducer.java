package com.harbor.secondHand.producer;

import com.harbor.secondHand.config.RabbitMQConfig;
import com.harbor.secondHand.domain.dto.ItemMessageDTO;
import com.harbor.secondHand.domain.po.ItemPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
      ItemMessageDTO message = new ItemMessageDTO();
      message.setItemId(itemPO.getId());
      message.setUserId(itemPO.getSellerId());
      message.setTitle(itemPO.getTitle());
      message.setDescription(itemPO.getDescription());
      message.setPrice(itemPO.getPrice());
      message.setStatus(itemPO.getStatus());
      message.setUpdateTime(LocalDateTime.now());
      message.setOperationType(operationType);

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
