package com.harbor.secondHand.producer;

import com.harbor.secondHand.config.RabbitMQConfig;
import com.harbor.secondHand.domain.dto.OrderMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class OrderMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送订单信息变化消息
     *
     * @param orderMessageDTO 订单消息DTO
     * @param operationType   操作类型：CREATE, UPDATE, CANCEL
     */
    public void sendOrderMessage(OrderMessageDTO orderMessageDTO, String operationType) {
        try {
            orderMessageDTO.setOperationType(operationType);
            orderMessageDTO.setCreateTime(LocalDateTime.now());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ORDER_ROUTING_KEY,
                    orderMessageDTO);
            log.info("发送订单信息变化消息成功，orderId: {}, operationType: {}", orderMessageDTO.getOrderId(), operationType);
        } catch (Exception e) {
            log.error("发送订单信息变化消息失败，orderId: {}, error: {}", orderMessageDTO.getOrderId(), e.getMessage(), e);
        }
    }
}
