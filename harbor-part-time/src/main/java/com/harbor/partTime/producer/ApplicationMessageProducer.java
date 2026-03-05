package com.harbor.partTime.producer;

import com.harbor.partTime.config.RabbitMQConfig;
import com.harbor.partTime.domain.dto.ApplicationApproveMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 兼职申请消息生产者
 * 用于发送申请审批相关的消息通知
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 消息类型枚举
     */
    public enum MessageType {
        APPROVE("approve", "恭喜！您的兼职申请【%s】已通过审核，请尽快联系雇主确认入职事宜。"),
        REJECT("reject", "很抱歉，您的兼职申请【%s】未通过审核，请继续关注其他兼职机会。"),
        NEW_APPLY("new_apply", "您发布的兼职【%s】收到了新的申请，请注意查看并及时处理。");

        private final String type;
        private final String messageTemplate;

        MessageType(String type, String messageTemplate) {
            this.type = type;
            this.messageTemplate = messageTemplate;
        }

        public String getType() {
            return type;
        }

        public String getMessageTemplate() {
            return messageTemplate;
        }
    }

    /**
     * 发送申请审批通过消息
     *
     * @param applicationId 申请ID
     * @param partTimeId    兼职ID
     * @param userId        申请者用户ID（消息接收者）
     * @param partTimeTitle 兼职标题
     */
    public void sendApplicationApproveMessage(Long applicationId, Long partTimeId, Long userId, String partTimeTitle) {
        sendMessage(applicationId, partTimeId, userId, partTimeTitle, MessageType.APPROVE);
    }

    /**
     * 发送申请审批拒绝消息
     *
     * @param applicationId 申请ID
     * @param partTimeId    兼职ID
     * @param userId        申请者用户ID（消息接收者）
     * @param partTimeTitle 兼职标题
     */
    public void sendApplicationRejectMessage(Long applicationId, Long partTimeId, Long userId, String partTimeTitle) {
        sendMessage(applicationId, partTimeId, userId, partTimeTitle, MessageType.REJECT);
    }

    /**
     * 发送新申请通知消息给发布者
     *
     * @param applicationId 申请ID
     * @param partTimeId    兼职ID
     * @param publisherId   发布者用户ID（消息接收者）
     * @param partTimeTitle 兼职标题
     */
    public void sendNewApplicationMessage(Long applicationId, Long partTimeId, Long publisherId, String partTimeTitle) {
        sendMessage(applicationId, partTimeId, publisherId, partTimeTitle, MessageType.NEW_APPLY);
    }

    /**
     * 发送申请审批消息
     *
     * @param applicationId 申请ID
     * @param partTimeId    兼职ID
     * @param userId        申请者用户ID（消息接收者）
     * @param partTimeTitle 兼职标题
     * @param messageType   消息类型
     */
    private void sendMessage(Long applicationId, Long partTimeId, Long userId, String partTimeTitle, MessageType messageType) {
        try {
            ApplicationApproveMessageDTO message = new ApplicationApproveMessageDTO();
            message.setMessageId(generateMessageId(applicationId, messageType));
            message.setApplicationId(applicationId);
            message.setPartTimeId(partTimeId);
            message.setUserId(userId);
            message.setPartTimeTitle(partTimeTitle);
            message.setMessage(buildMessage(partTimeTitle, messageType));
            message.setStatus(0);
            message.setCreateTime(LocalDateTime.now());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.APPLICATION_ROUTING_KEY,
                    message);
            log.info("发送申请审批{}消息成功，applicationId: {}, userId: {}", 
                    messageType.getType(), applicationId, userId);
        } catch (Exception e) {
            log.error("发送申请审批{}消息失败，applicationId: {}, error: {}", 
                    messageType.getType(), applicationId, e.getMessage(), e);
        }
    }

    /**
     * 生成消息唯一ID
     * 格式：apply_类型_时间戳_随机数
     *
     * @param applicationId 申请ID
     * @param messageType   消息类型
     * @return 消息唯一ID
     */
    private String generateMessageId(Long applicationId, MessageType messageType) {
        return String.format("apply_%s_%d_%d_%s",
                messageType.getType(),
                applicationId,
                System.currentTimeMillis(),
                UUID.randomUUID().toString().substring(0, 8));
    }

    /**
     * 构建消息内容
     *
     * @param partTimeTitle 兼职标题
     * @param messageType   消息类型
     * @return 消息内容
     */
    private String buildMessage(String partTimeTitle, MessageType messageType) {
        return String.format(messageType.getMessageTemplate(), partTimeTitle);
    }
}
