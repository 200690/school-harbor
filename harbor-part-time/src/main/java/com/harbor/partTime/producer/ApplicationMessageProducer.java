package com.harbor.partTime.producer;

import com.harbor.partTime.config.RabbitMQConfig;
import com.harbor.partTime.domain.dto.ApplicationApproveMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 兼职申请消息生产者
 * 用于发送申请审批相关的消息通知
 */
@Slf4j
@Component
public class ApplicationMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送申请审批通过消息
     *
     * @param applicationId 申请ID
     * @param partTimeId    兼职ID
     * @param userId        申请者用户ID（消息接收者）
     * @param partTimeTitle 兼职标题
     */
    public void sendApplicationApproveMessage(Long applicationId, Long partTimeId, Long userId, String partTimeTitle) {
        try {
            // 构建消息DTO
            ApplicationApproveMessageDTO message = new ApplicationApproveMessageDTO();
            message.setMessageId(generateMessageId(applicationId));
            message.setApplicationId(applicationId);
            message.setPartTimeId(partTimeId);
            message.setUserId(userId);
            message.setPartTimeTitle(partTimeTitle);
            message.setMessage(buildApproveMessage(partTimeTitle));
            message.setStatus(0);
            message.setCreateTime(LocalDateTime.now());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.APPLICATION_ROUTING_KEY,
                    message);
            log.info("发送申请审批通过消息成功，applicationId: {}, userId: {}", applicationId, userId);
        } catch (Exception e) {
            log.error("发送申请审批通过消息失败，applicationId: {}, error: {}", applicationId, e.getMessage(), e);
        }
    }

    /**
     * 生成消息唯一ID
     * 格式：apply_时间戳_随机数
     *
     * @param applicationId 申请ID
     * @return 消息唯一ID
     */
    private String generateMessageId(Long applicationId) {
        return String.format("apply_%d_%d_%s",
                applicationId,
                System.currentTimeMillis(),
                UUID.randomUUID().toString().substring(0, 8));
    }

    /**
     * 构建审批通过消息内容
     *
     * @param partTimeTitle 兼职标题
     * @return 消息内容
     */
    private String buildApproveMessage(String partTimeTitle) {
        return String.format("恭喜！您的兼职申请【%s】已通过审核，请尽快联系雇主确认入职事宜。", partTimeTitle);
    }
}
