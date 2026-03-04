package com.harbor.partTime.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 兼职申请审批通过消息DTO
 * 用于消息队列传输申请审批通知
 */
@Data
public class ApplicationApproveMessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息唯一ID，用于幂等性校验
     * 格式：业务前缀_时间戳_随机数，如：apply_1679123456789_1234
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
     * 例如："恭喜！您的兼职申请【xxx】已通过审核"
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
