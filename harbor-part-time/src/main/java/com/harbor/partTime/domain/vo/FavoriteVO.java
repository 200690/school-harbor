package com.harbor.partTime.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class FavoriteVO {
    // 收藏相关信息
    private Long favoriteId;           // 收藏ID
    private LocalDateTime favoriteTime; // 收藏时间

    // 兼职基本信息
    private Long partTimeId;            // 兼职ID
    private String title;               // 兼职标题
    private String employer;             // 招聘方
    private String location;             // 工作地点
    private String workTime;             // 工作时间
    private String salaryUnit;           // 薪资单位
    private String salaryDesc;           // 薪资说明
    private Integer type;                // 兼职类型

    // 兼职状态信息
    private Integer status;              // 状态（0:已下架, 1:招聘中, 2:已招满, 3:已截止）

    // 统计数据
    private Integer viewCount;           // 浏览次数
    private Integer applicantCount;      // 申请人数
    private Integer creditScore;         // 信誉分

    // 发布时间
    private LocalDateTime publishTime;   // 发布时间
}
