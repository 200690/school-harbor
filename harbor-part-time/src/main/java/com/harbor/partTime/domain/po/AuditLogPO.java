package com.harbor.partTime.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("part_time_audit_log")
public class AuditLogPO {
    @TableId(type = IdType.AUTO)
    private Long id;                       // 审核记录ID
    private Long partTimeId;                // 兼职ID
    private Long operatorId;                 // 操作人ID
    private String action;                   // 操作：submit-提交审核，pass-通过，reject-拒绝，offline-下架
    private String reason;                   // 原因/备注
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;        // 操作时间
}