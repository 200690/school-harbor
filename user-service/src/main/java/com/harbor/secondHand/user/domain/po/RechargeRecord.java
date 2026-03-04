package com.harbor.secondHand.user.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 充值记录实体类
 */
@Data
@TableName("recharge_record")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RechargeRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 充值单号（格式：RC + yyyyMMdd + 6位流水号）
     */
    private String rechargeNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 充值金额
     */
    private int amount;

    /**
     * 状态：0-处理中 1-成功 2-失败
     */
    private Integer status;

    /**
     * 充值前余额
     */
    private int beforeBalance;

    /**
     * 充值后余额
     */
    private int afterBalance;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}