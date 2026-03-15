package com.harbor.pay.consumer;

import com.harbor.pay.config.RabbitMQConfig;
import com.harbor.pay.domain.dto.OrderMessageDTO;
import com.harbor.pay.service.IPayOrderService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 订单消息消费者
 * 处理订单创建消息，创建支付订单
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final IPayOrderService payOrderService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE_NAME)
    public void handleOrderMessage(OrderMessageDTO orderMessageDTO, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("接收到订单信息变化消息: {}", orderMessageDTO);

            // 处理消息
            processOrderMessage(orderMessageDTO);

            // 确认消息
            channel.basicAck(deliveryTag, false);
            log.info("订单信息变化消息处理成功，orderId: {}", orderMessageDTO.getOrderId());
        } catch (Exception e) {
            log.error("处理订单信息变化消息失败: {}", e.getMessage(), e);
            // 拒绝消息，重新入队
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 处理订单消息
     *
     * @param orderMessageDTO 订单消息
     */
    private void processOrderMessage(OrderMessageDTO orderMessageDTO) {
        // 根据操作类型处理订单消息
        switch (orderMessageDTO.getOperationType()) {
            case "CREATE":
                // 处理订单创建消息，创建支付订单
                log.info("处理订单创建消息，创建支付订单，orderId: {}", orderMessageDTO.getOrderId());
                payOrderService.createPayOrderFromMessage(orderMessageDTO);
                break;
            case "UPDATE":
                // 处理订单更新消息
                log.info("处理订单更新消息，orderId: {}", orderMessageDTO.getOrderId());
                break;
            case "CANCEL":
                // 处理订单取消消息
                log.info("处理订单取消消息，orderId: {}", orderMessageDTO.getOrderId());
                break;
            default:
                log.warn("未知的订单操作类型: {}", orderMessageDTO.getOperationType());
        }
    }
}
