package com.harbor.messageservice.domain.message;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 兼职申请审批消息
 * 用于接收申请审批相关的消息通知
 */
@Data
public class ApplicationMessage {

    /**
     * 消息唯一ID，用于幂等性校验
     */
    private String messageId;

    /**
     * 申请ID
     */
    private Long applicationId;

    /**
     * 兼职ID
     */
    private Long partTimeId;

    /**
     * 申请者用户ID（消息接收者）
     */
    private Long userId;

    /**
     * 兼职标题
     */
    private String partTimeTitle;

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
