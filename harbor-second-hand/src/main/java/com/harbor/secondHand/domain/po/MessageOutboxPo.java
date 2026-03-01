package com.harbor.secondHand.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 消息发件箱表 持久化对象
 */
@Data
@TableName("message_outbox")
@Accessors(chain = true)
public class MessageOutboxPo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 业务ID（如job_id） */
    private String aggregateId;

    /** 事件类型：ITEM_CREATED, ITEM_UPDATED, ITEM_DELETED */
    private String eventType;

    /** 消息内容（包含变更后的数据） */
    private String payload;

    /** 状态：0-待发送 1-已发送 */
    @TableField("status")
    private Integer status;

    /** 重试次数 */
    private Integer retryCount;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 发送时间 */
    private Date sendTime;
}