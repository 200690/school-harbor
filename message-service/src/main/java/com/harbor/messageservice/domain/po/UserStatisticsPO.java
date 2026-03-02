package com.harbor.messageservice.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户统计持久化对象
 * 对应数据库表 user_statistics
 *
 * @author yourname
 * @date 2026-03-02
 */
@Data
@Accessors(chain = true)
@TableName("user_statistics")
public class UserStatisticsPO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 兼职发布数
     */
    private Integer jobPostCount;

    /**
     * 兼职收藏数
     */
    private Integer jobFavoriteCount;

    /**
     * 兼职申请数
     */
    private Integer jobApplyCount;

    /**
     * 二手发布数
     */
    private Integer itemPostCount;

    /**
     * 二手收藏数
     */
    private Integer itemFavoriteCount;

    /**
     * 二手购买数
     */
    private Integer itemPurchaseCount;

    /**
     * 二手申请数
     */
    private Integer itemApplyCount;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 无参构造方法
     */
    public UserStatisticsPO() {
    }

    /**
     * 增加兼职发布数
     */
    public void incrementJobPostCount(int delta) {
        if (this.jobPostCount == null) {
            this.jobPostCount = 0;
        }
        this.jobPostCount += delta;
    }

    /**
     * 增加兼职收藏数
     */
    public void incrementJobFavoriteCount(int delta) {
        if (this.jobFavoriteCount == null) {
            this.jobFavoriteCount = 0;
        }
        this.jobFavoriteCount += delta;
    }

    /**
     * 增加兼职申请数
     */
    public void incrementJobApplyCount(int delta) {
        if (this.jobApplyCount == null) {
            this.jobApplyCount = 0;
        }
        this.jobApplyCount += delta;
    }

    /**
     * 增加二手发布数
     */
    public void incrementItemPostCount(int delta) {
        if (this.itemPostCount == null) {
            this.itemPostCount = 0;
        }
        this.itemPostCount += delta;
    }

    /**
     * 增加二手收藏数
     */
    public void incrementItemFavoriteCount(int delta) {
        if (this.itemFavoriteCount == null) {
            this.itemFavoriteCount = 0;
        }
        this.itemFavoriteCount += delta;
    }

    /**
     * 增加二手购买数
     */
    public void incrementItemPurchaseCount(int delta) {
        if (this.itemPurchaseCount == null) {
            this.itemPurchaseCount = 0;
        }
        this.itemPurchaseCount += delta;
    }

    /**
     * 增加二手申请数
     */
    public void incrementItemApplyCount(int delta) {
        if (this.itemApplyCount == null) {
            this.itemApplyCount = 0;
        }
        this.itemApplyCount += delta;
    }
}