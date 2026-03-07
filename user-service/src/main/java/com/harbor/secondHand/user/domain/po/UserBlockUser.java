package com.harbor.secondHand.user.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户拉黑商品表持久化对象
 */
@TableName("user_block_user")
@Data
@Accessors(chain = true)
public class UserBlockUser {

    public static final Integer STATS_ACTIVE = 1;
    public static final Integer STATS_INACTIVE = 0;
    /**
     * 拉黑记录ID
     */
    private Long id;

    /**
     * 用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 被拉黑用户ID
     */
    @NotNull
    private Long blockId;

    /**
     * 状态：0-未生效（已取消拉黑），1-生效中
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
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}