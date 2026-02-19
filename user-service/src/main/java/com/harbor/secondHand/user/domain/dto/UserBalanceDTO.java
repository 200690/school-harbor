package com.harbor.secondHand.user.domain.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额实体类
 */
@Data
@TableName("user_balance")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBalanceDTO {

    /**
     * 用户ID（主键）
     */
    @TableId(type = IdType.INPUT)  // 手动输入，不从数据库自增
    private Long userId;

    /**
     * 当前余额
     */
    private BigDecimal balance;

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
    private Integer version;

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

    // ========== 业务方法 ==========

    /**
     * 增加余额（充值）
     */
    public void increaseBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("充值金额必须大于0");
        }
        this.balance = this.balance.add(amount);
        this.totalRecharge = this.totalRecharge.add(amount);
    }

    /**
     * 减少余额（消费）
     */
    public void decreaseBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("消费金额必须大于0");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("余额不足");
        }
        this.balance = this.balance.subtract(amount);
        this.totalConsume = this.totalConsume.add(amount);
    }

    /**
     * 是否有足够余额
     */
    public boolean hasEnoughBalance(BigDecimal amount) {
        return this.balance.compareTo(amount) >= 0;
    }
}