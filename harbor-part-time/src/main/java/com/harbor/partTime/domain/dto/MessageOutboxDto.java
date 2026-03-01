package com.harbor.partTime.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息发件箱 数据传输对象
 */
@Data
@Accessors(chain = true)
public class MessageOutboxDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键ID（可选，视传输场景而定） */
    private Long id;

    /** 业务ID（如job_id） */
    private String aggregateId;

    /** 事件类型 */
    private String eventType;

    /** 消息内容（JSON字符串） */
    private String payload;

    /** 状态：0-待发送 1-已发送 */
    private Integer status;

    /** 重试次数 */
    private Integer retryCount;

    /** 创建时间 */
    private Date createTime;

    /** 发送时间 */
    private Date sendTime;
}