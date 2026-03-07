package com.harbor.messageservice.consumer;

import com.harbor.messageservice.config.RabbitMQConfig;
import com.harbor.messageservice.domain.po.MessageProcessedPO;
import com.harbor.messageservice.mapper.MsgProcessedMapper;
import com.harbor.utils.dto.CommentMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentNotificationConsumer {

    private final MsgProcessedMapper msgProcessedMapper;

    @RabbitListener(queues = RabbitMQConfig.COMMENT_NOTIFICATION_QUEUE_NAME)
    public void handleCommentNotification(CommentMessageDTO messageDTO) {
        log.info("接收到评论通知消息: toUserId={}, message={}", 
                messageDTO.getToUserId(), messageDTO.getMessage());
        
        try {
            MessageProcessedPO notificationPO = new MessageProcessedPO();
            notificationPO.setId(UUID.randomUUID().toString());
            notificationPO.setMessage(messageDTO.getMessage());
            notificationPO.setUserId(messageDTO.getToUserId());
            notificationPO.setStatus(0);
            notificationPO.setProcessedTime(LocalDateTime.now());
            
            msgProcessedMapper.insert(notificationPO);
            
            log.info("评论通知消息处理成功: id={}, toUserId={}", 
                    notificationPO.getId(), messageDTO.getToUserId());
        } catch (Exception e) {
            log.error("评论通知消息处理失败: toUserId={}, error={}", 
                    messageDTO.getToUserId(), e.getMessage(), e);
            throw e;
        }
    }
}
