package com.harbor.reportservice.domain.po;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

/**
 * 举报处理记录表持久化对象
 */
@Data
@Accessors(chain = true)
@TableName("report_handles")
public class ReportHandlePO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 举报ID
     */
    private Long reportId;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理动作：1-删除内容，2-警告用户，3-封禁用户，4-忽略
     */
    private Integer action;

    /**
     * 处理说明
     */
    private String handleDesc;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}