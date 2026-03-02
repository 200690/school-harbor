package com.harbor.messageservice.domain.dto;

import com.harbor.messageservice.domain.po.UserStatisticsPO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户统计数据传输对象
 * 用于服务间通信和消息队列传输
 *
 * @author yourname
 * @date 2026-03-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatisticsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 兼职发布数
     */
    @Min(value = 0, message = "兼职发布数不能小于0")
    private Integer jobPostCount;

    /**
     * 兼职收藏数
     */
    @Min(value = 0, message = "兼职收藏数不能小于0")
    private Integer jobFavoriteCount;

    /**
     * 兼职申请数
     */
    @Min(value = 0, message = "兼职申请数不能小于0")
    private Integer jobApplyCount;

    /**
     * 二手发布数
     */
    @Min(value = 0, message = "二手发布数不能小于0")
    private Integer itemPostCount;

    /**
     * 二手收藏数
     */
    @Min(value = 0, message = "二手收藏数不能小于0")
    private Integer itemFavoriteCount;

    /**
     * 二手购买数
     */
    @Min(value = 0, message = "二手购买数不能小于0")
    private Integer itemPurchaseCount;

    /**
     * 二手申请数
     */
    @Min(value = 0, message = "二手申请数不能小于0")
    private Integer itemApplyCount;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 操作类型枚举（用于消息队列）
     */
    private OperationType operationType;

    /**
     * 操作增量（正数表示增加，负数表示减少）
     */
    private Integer delta;

    /**
     * 操作类型枚举
     */
    public enum OperationType {
        JOB_POST_INCREMENT,      // 兼职发布数增加
        JOB_POST_DECREMENT,      // 兼职发布数减少
        JOB_FAVORITE_INCREMENT,  // 兼职收藏数增加
        JOB_FAVORITE_DECREMENT,  // 兼职收藏数减少
        JOB_APPLY_INCREMENT,     // 兼职申请数增加
        JOB_APPLY_DECREMENT,     // 兼职申请数减少
        ITEM_POST_INCREMENT,     // 二手发布数增加
        ITEM_POST_DECREMENT,     // 二手发布数减少
        ITEM_FAVORITE_INCREMENT, // 二手收藏数增加
        ITEM_FAVORITE_DECREMENT, // 二手收藏数减少
        ITEM_PURCHASE_INCREMENT, // 二手购买数增加
        ITEM_PURCHASE_DECREMENT, // 二手购买数减少
        ITEM_APPLY_INCREMENT,    // 二手申请数增加
        ITEM_APPLY_DECREMENT     // 二手申请数减少
    }

    /**
     * 从PO转换为DTO
     */
    public static UserStatisticsDTO fromPO(UserStatisticsPO po) {
        if (po == null) {
            return null;
        }
        return UserStatisticsDTO.builder()
                .userId(po.getUserId())
                .jobPostCount(po.getJobPostCount())
                .jobFavoriteCount(po.getJobFavoriteCount())
                .jobApplyCount(po.getJobApplyCount())
                .itemPostCount(po.getItemPostCount())
                .itemFavoriteCount(po.getItemFavoriteCount())
                .itemPurchaseCount(po.getItemPurchaseCount())
                .itemApplyCount(po.getItemApplyCount())
                .updateTime(po.getUpdateTime())
                .build();
    }

    /**
     * 创建增量操作DTO
     */
    public static UserStatisticsDTO createIncrementDTO(Long userId,
                                                       OperationType operationType,
                                                       int delta) {
        return UserStatisticsDTO.builder()
                .userId(userId)
                .operationType(operationType)
                .delta(delta)
                .build();
    }

    /**
     * 创建全量更新DTO
     */
    public static UserStatisticsDTO createFullDTO(UserStatisticsPO po) {
        return fromPO(po);
    }
}