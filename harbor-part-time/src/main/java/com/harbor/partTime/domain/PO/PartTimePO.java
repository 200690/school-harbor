package com.harbor.partTime.domain.PO;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 兼职持久化对象
 */
@Data
@TableName("part_time")
public class PartTimePO {

    @TableId(type = IdType.AUTO)
    private Long id;                     // 兼职ID

    @NotNull
    private String title;                 // 兼职标题

    @NotNull
    private String employer;              // 招聘方

    @NotNull
    private String location;              // 工作地点

    @NotNull
    private String workTime;              // 工作时间

    @NotNull
    private String salaryUnit;             // 薪资单位（元/小时, 元/天, 元/月）

    private String salaryDesc;             // 薪资说明（如：200-300元/天，周结）

    @NotNull
    private Integer type;                  // 兼职类型（1:校内, 2:校外, 3:实习）

    private String description;            // 兼职描述

    private String requirements;           // 招聘要求

    @NotNull
    private String contactPerson;          // 联系人

    @NotNull
    private String contactPhone;           // 联系电话

    private Long publisherId;              // 发布者ID

    private Integer status;                // 状态（0:已下架, 1:招聘中, 2:已招满, 3:已截止）

    @TableLogic
    private Integer isDelete;              // 是否删除（0未删除，1已删除）

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime publishTime;     // 发布时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;      // 更新时间

    private Integer viewCount;             // 浏览次数

    private Integer applicantCount;        // 申请人数

    private Integer creditScore;           // 信誉分（0-100）
}