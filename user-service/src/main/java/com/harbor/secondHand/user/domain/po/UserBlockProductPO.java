package com.harbor.secondHand.user.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户拉黑商品持久化对象
 * 对应数据库表：user_block_product
 * 功能：记录用户拉黑的商品
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_block_product")
public class UserBlockProductPO {

    /**
     * 拉黑记录ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 是否生效
     * 0-未生效（已取消拉黑）
     * 1-生效中（正在拉黑）
     */
    private Integer stats;

    /**
     * 拉黑时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime blockTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ========== 业务字段常量 ==========

    /**
     * 状态常量：未生效（已取消拉黑）
     */
    public static final int STATS_INACTIVE = 0;

    /**
     * 状态常量：生效中（正在拉黑）
     */
    public static final int STATS_ACTIVE = 1;

    // ========== 辅助方法 ==========

    /**
     * 判断是否生效中
     */
    public boolean isActive() {
        return STATS_ACTIVE == this.stats;
    }

    /**
     * 判断是否已取消
     */
    public boolean isInactive() {
        return STATS_INACTIVE == this.stats;
    }

    /**
     * 生效拉黑
     */
    public void active() {
        this.stats = STATS_ACTIVE;
    }

    /**
     * 取消拉黑
     */
    public void inactive() {
        this.stats = STATS_INACTIVE;
    }
}