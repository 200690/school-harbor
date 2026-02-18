package com.harbor.user.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("user_balance")
public class UserBalance {
    /**
     * 用户ID（主键，与用户表1对1）
     */
    @TableId(type = IdType.INPUT)
    private Long userId;

    /**
     * 当前余额（单位：元）
     */
    private int balance;

    /**
     * 累计充值总额
     */
    private BigDecimal totalRecharge;

    /**
     * 累计消费总额
     */
    private BigDecimal totalConsume;

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version = 1;

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

    /**
     * 冻结余额
     */
    private int frozen_balance;
}
