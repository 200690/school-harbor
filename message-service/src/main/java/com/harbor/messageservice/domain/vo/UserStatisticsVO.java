package com.harbor.messageservice.domain.vo;

import com.harbor.messageservice.domain.po.UserStatisticsPO;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用户统计视图对象
 * 用于返回给前端的数据结构
 *
 * @author yourname
 * @date 2026-03-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户统计信息", description = "用户在个人中心展示的各项统计数据")
public class UserStatisticsVO {

    @ApiModelProperty(value = "用户ID", example = "10001")
    private Long userId;

    @ApiModelProperty(value = "兼职发布数", example = "5")
    private Integer jobPostCount;

    @ApiModelProperty(value = "兼职收藏数", example = "12")
    private Integer jobFavoriteCount;

    @ApiModelProperty(value = "兼职申请数", example = "3")
    private Integer jobApplyCount;

    @ApiModelProperty(value = "二手发布数", example = "8")
    private Integer itemPostCount;

    @ApiModelProperty(value = "二手收藏数", example = "15")
    private Integer itemFavoriteCount;

    @ApiModelProperty(value = "二手购买数", example = "2")
    private Integer itemPurchaseCount;

    @ApiModelProperty(value = "二手申请数", example = "4")
    private Integer itemApplyCount;

    @ApiModelProperty(value = "统计数据更新时间", example = "2026-03-02 14:30:00")
    private String updateTime;

    @ApiModelProperty(value = "总发布数", example = "13")
    private Integer totalPostCount;

    @ApiModelProperty(value = "总收藏数", example = "27")
    private Integer totalFavoriteCount;

    @ApiModelProperty(value = "总申请数", example = "7")
    private Integer totalApplyCount;

    /**
     * 从PO转换为VO
     */
    public static UserStatisticsVO fromPO(UserStatisticsPO po) {
        if (po == null) {
            return null;
        }

        UserStatisticsVO vo = UserStatisticsVO.builder()
                .userId(po.getUserId())
                .jobPostCount(po.getJobPostCount())
                .jobFavoriteCount(po.getJobFavoriteCount())
                .jobApplyCount(po.getJobApplyCount())
                .itemPostCount(po.getItemPostCount())
                .itemFavoriteCount(po.getItemFavoriteCount())
                .itemPurchaseCount(po.getItemPurchaseCount())
                .itemApplyCount(po.getItemApplyCount())
                .build();

        // 计算汇总数据
        vo.calculateTotals();

        // 格式化时间
        if (po.getUpdateTime() != null) {
            vo.setUpdateTime(po.getUpdateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        return vo;
    }

    /**
     * 计算总计数
     */
    private void calculateTotals() {
        this.totalPostCount =
                (jobPostCount != null ? jobPostCount : 0) +
                        (itemPostCount != null ? itemPostCount : 0);

        this.totalFavoriteCount =
                (jobFavoriteCount != null ? jobFavoriteCount : 0) +
                        (itemFavoriteCount != null ? itemFavoriteCount : 0);

        this.totalApplyCount =
                (jobApplyCount != null ? jobApplyCount : 0) +
                        (itemApplyCount != null ? itemApplyCount : 0);
    }

    /**
     * 判断是否有兼职数据
     */
    public boolean hasJobData() {
        return (jobPostCount != null && jobPostCount > 0) ||
                (jobFavoriteCount != null && jobFavoriteCount > 0) ||
                (jobApplyCount != null && jobApplyCount > 0);
    }

    /**
     * 判断是否有二手数据
     */
    public boolean hasItemData() {
        return (itemPostCount != null && itemPostCount > 0) ||
                (itemFavoriteCount != null && itemFavoriteCount > 0) ||
                (itemPurchaseCount != null && itemPurchaseCount > 0) ||
                (itemApplyCount != null && itemApplyCount > 0);
    }

    /**
     * 获取格式化的统计摘要
     */
    public String getSummary() {
        return String.format("发布了%d个兼职，%d个二手；收藏了%d个兼职，%d个二手；申请了%d个兼职，%d个二手；购买了%d个二手",
                jobPostCount != null ? jobPostCount : 0,
                itemPostCount != null ? itemPostCount : 0,
                jobFavoriteCount != null ? jobFavoriteCount : 0,
                itemFavoriteCount != null ? itemFavoriteCount : 0,
                jobApplyCount != null ? jobApplyCount : 0,
                itemApplyCount != null ? itemApplyCount : 0,
                itemPurchaseCount != null ? itemPurchaseCount : 0);
    }
}