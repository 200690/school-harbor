package com.harbor.secondHand.producer;

import com.harbor.secondHand.config.RabbitMQConfig;
import com.harbor.secondHand.domain.dto.PublishNotificationMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 发布通知消息生产者
 * 用于发送发布成功的通知消息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PublishNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 业务类型枚举
     */
    public enum BusinessType {
        JOB("兼职"),
        ITEM("二手商品");

        private final String displayName;

        BusinessType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * 发送二手商品发布成功通知
     *
     * @param itemId   商品ID
     * @param userId   发布者用户ID
     * @param title    商品标题
     */
    public void sendItemPublishNotification(Long itemId, Long userId, String title) {
        sendPublishNotification(itemId, userId, title, BusinessType.ITEM);
    }

    /**
     * 发送删除商品成功通知
     *
     * @param itemId   商品ID
     * @param userId   发布者用户ID
     * @param title    商品标题
     */
    public void sendItemDeleteNotification(Long itemId, Long userId, String title) {
        sendDeleteNotification(itemId, userId, title, BusinessType.ITEM);
    }

    /**
     * 发送订单创建成功通知（用于买家）
     *
     * @param orderId  订单ID
     * @param userId   用户ID
     * @param message  消息内容
     */
    public void sendOrderCreateNotificationForBuyer(Long orderId, Long userId, String message) {
        sendCustomNotification(orderId, userId, message, BusinessType.ITEM);
    }

    /**
     * 发送订单创建成功通知（用于卖家）
     *
     * @param orderId  订单ID
     * @param userId   用户ID
     * @param message  消息内容
     */
    public void sendOrderCreateNotificationForSeller(Long orderId, Long userId, String message) {
        sendCustomNotification(orderId, userId, message, BusinessType.ITEM);
    }

    /**
     * 发送自定义通知消息
     *
     * @param businessId   业务ID
     * @param userId       用户ID
     * @param message      消息内容
     * @param businessType 业务类型
     */
    private void sendCustomNotification(Long businessId, Long userId, String message, BusinessType businessType) {
        try {
            PublishNotificationMessageDTO messageDTO = new PublishNotificationMessageDTO();
            messageDTO.setMessageId(generateMessageId(businessId, businessType));
            messageDTO.setBusinessId(businessId);
            messageDTO.setUserId(userId);
            messageDTO.setTitle("");
            messageDTO.setBusinessType(businessType.name());
            messageDTO.setMessage(message);
            messageDTO.setStatus(0);
            messageDTO.setCreateTime(LocalDateTime.now());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.PUBLISH_NOTIFICATION_ROUTING_KEY,
                    messageDTO);
            log.info("发送{}自定义通知消息成功，businessId: {}, userId: {}",
                    businessType.getDisplayName(), businessId, userId);
        } catch (Exception e) {
            log.error("发送{}自定义通知消息失败，businessId: {}, error: {}",
                    businessType.getDisplayName(), businessId, e.getMessage(), e);
        }
    }

    /**
     * 发送发布成功通知消息
     *
     * @param businessId   业务ID
     * @param userId       发布者用户ID
     * @param title        标题
     * @param businessType 业务类型
     */
    private void sendPublishNotification(Long businessId, Long userId, String title, BusinessType businessType) {
        try {
            PublishNotificationMessageDTO message = new PublishNotificationMessageDTO();
            message.setMessageId(generateMessageId(businessId, businessType));
            message.setBusinessId(businessId);
            message.setUserId(userId);
            message.setTitle(title);
            message.setBusinessType(businessType.name());
            message.setMessage(buildMessage(title, businessType));
            message.setStatus(0);
            message.setCreateTime(LocalDateTime.now());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.PUBLISH_NOTIFICATION_ROUTING_KEY,
                    message);
            log.info("发送{}发布成功通知消息成功，businessId: {}, userId: {}",
                    businessType.getDisplayName(), businessId, userId);
        } catch (Exception e) {
            log.error("发送{}发布成功通知消息失败，businessId: {}, error: {}",
                    businessType.getDisplayName(), businessId, e.getMessage(), e);
        }
    }

    /**
     * 生成消息唯一ID
     * 格式：publish_业务类型_业务ID_时间戳_随机数
     *
     * @param businessId   业务ID
     * @param businessType 业务类型
     * @return 消息唯一ID
     */
    private String generateMessageId(Long businessId, BusinessType businessType) {
        return String.format("publish_%s_%d_%d_%s",
                businessType.name().toLowerCase(),
                businessId,
                System.currentTimeMillis(),
                UUID.randomUUID().toString().substring(0, 8));
    }

    /**
     * 构建消息内容
     *
     * @param title        标题
     * @param businessType 业务类型
     * @return 消息内容
     */
    private String buildMessage(String title, BusinessType businessType) {
        return String.format("恭喜！您的%s【%s】已成功发布，请耐心等待审核或买家联系。",
                businessType.getDisplayName(), title);
    }

    /**
     * 发送删除通知消息
     *
     * @param businessId   业务ID
     * @param userId       发布者用户ID
     * @param title        标题
     * @param businessType 业务类型
     */
    private void sendDeleteNotification(Long businessId, Long userId, String title, BusinessType businessType) {
        try {
            PublishNotificationMessageDTO message = new PublishNotificationMessageDTO();
            message.setMessageId(generateMessageId(businessId, businessType));
            message.setBusinessId(businessId);
            message.setUserId(userId);
            message.setTitle(title);
            message.setBusinessType(businessType.name());
            message.setMessage(buildDeleteMessage(title, businessType));
            message.setStatus(0);
            message.setCreateTime(LocalDateTime.now());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.PUBLISH_NOTIFICATION_ROUTING_KEY,
                    message);
            log.info("发送{}删除通知消息成功，businessId: {}, userId: {}",
                    businessType.getDisplayName(), businessId, userId);
        } catch (Exception e) {
            log.error("发送{}删除通知消息失败，businessId: {}, error: {}",
                    businessType.getDisplayName(), businessId, e.getMessage(), e);
        }
    }

    /**
     * 构建删除消息内容
     *
     * @param title        标题
     * @param businessType 业务类型
     * @return 消息内容
     */
    private String buildDeleteMessage(String title, BusinessType businessType) {
        return String.format("您发布的%s【%s】已被管理员下架。",
                businessType.getDisplayName(), title);
    }
}
