package com.harbor.partTime.domain.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 消息发件箱 视图对象
 */
@Data
@Accessors(chain = true)
public class MessageOutboxVo {

    private Long id;

    /** 业务ID */
    private String aggregateId;

    /** 事件类型 */
    private String eventType;

    /** 消息内容 */
    private String payload;

    /** 状态（0-待发送，1-已发送） */
    private Integer status;

    /** 状态描述（根据status动态生成） */
    public String getStatusDesc() {
        if (status == null) return "";
        return switch (status) {
            case 0 -> "待发送";
            case 1 -> "已发送";
            default -> "未知";
        };
    }

    /** 重试次数 */
    private Integer retryCount;

    /** 创建时间，格式化后返回 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;
}