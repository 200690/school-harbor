package com.harbor.secondHand.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 发布通知消息DTO
 * 用于消息队列传输发布成功的通知消息
 */
@Data
public class PublishNotificationMessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息唯一ID，用于幂等性校验
     * 格式：publish_业务类型_时间戳_随机数
     */
    private String messageId;

    /**
     * 业务ID（兼职ID或二手商品ID）
     */
    private Long businessId;

    /**
     * 发布者用户ID（消息接收者）
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 业务类型：JOB-兼职，ITEM-二手商品
     */
    private String businessType;

    /**
     * 消息通知内容
     */
    private String message;

    /**
     * 状态(0:未处理 1:处理中 2:处理成功 3:处理失败)
     */
    private Integer status;

    /**
     * 消息创建时间
     */
    private LocalDateTime createTime;

    /**
     * 通知时间（处理时间）
     */
    private LocalDateTime processedTime;
}
