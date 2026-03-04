package com.harbor.messageservice.consumer;

import com.harbor.messageservice.config.RabbitMQConfig;
import com.harbor.messageservice.domain.message.ApplicationMessage;
import com.harbor.messageservice.domain.po.MessageProcessedPO;
import com.harbor.messageservice.service.IMsgProcessedService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 兼职申请消息消费者
 * 处理申请审批相关的消息通知，并写入message_processed表
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationConsumer {

    private final IMsgProcessedService msgProcessedService;

    /**
     * 处理申请审批通过消息
     *
     * @param applicationMessage 申请审批消息
     * @param channel            RabbitMQ通道
     * @param deliveryTag        消息投递标签
     * @throws IOException 当消息确认失败时抛出
     */
    @RabbitListener(queues = RabbitMQConfig.APPLICATION_QUEUE_NAME)
    public void handleApplicationMessage(ApplicationMessage applicationMessage, Channel channel,
                                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.info("接收到申请审批消息: {}", applicationMessage);

        try {
            // 幂等性校验：检查消息是否已处理
            MessageProcessedPO existingRecord = msgProcessedService.getById(applicationMessage.getMessageId());
            if (existingRecord != null) {
                log.info("消息已处理过，跳过处理，messageId: {}", applicationMessage.getMessageId());
                channel.basicAck(deliveryTag, false);
                return;
            }

            // 处理消息：写入message_processed表
            processApplicationMessage(applicationMessage);

            // 确认消息
            channel.basicAck(deliveryTag, false);
            log.info("申请审批消息处理成功，messageId: {}", applicationMessage.getMessageId());
        } catch (Exception e) {
            log.error("处理申请审批消息失败: {}", e.getMessage(), e);
            // 拒绝消息，重新入队
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 处理申请审批消息，写入message_processed表
     *
     * @param applicationMessage 申请审批消息
     */
    private void processApplicationMessage(ApplicationMessage applicationMessage) {
        // 构建MessageProcessedPO对象
        MessageProcessedPO messageProcessed = MessageProcessedPO.builder()
                .id(applicationMessage.getMessageId())
                .message(applicationMessage.getMessage())
                .userId(applicationMessage.getUserId())
                .status(2)
                .processedTime(LocalDateTime.now())
                .build();

        // 保存到message_processed表
        msgProcessedService.save(messageProcessed);
        log.info("申请审批消息已写入message_processed表，messageId: {}, userId: {}",
                applicationMessage.getMessageId(), applicationMessage.getUserId());
    }
}
