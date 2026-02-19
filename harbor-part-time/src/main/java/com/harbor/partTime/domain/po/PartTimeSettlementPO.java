package com.harbor.partTime.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("part_time_settlement")
public class PartTimeSettlementPO {
    @TableId(type = IdType.AUTO)
    private Long id;                       // 结算记录ID
    private Long applicationId;              // 申请记录ID
    private Long userId;                     // 收款用户ID
    private BigDecimal amount;               // 结算金额
    private Integer status;                   // 状态：0-待结算，1-已结算，2-结算失败
    private String payOrderNo;                // 支付系统订单号
    private LocalDateTime settleTime;         // 实际结算时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;         // 记录创建时间
}