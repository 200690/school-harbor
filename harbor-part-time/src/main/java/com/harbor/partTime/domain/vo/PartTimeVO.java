package com.harbor.partTime.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 兼职视图对象
 */
@Accessors(chain = true)
@Data
public class PartTimeVO {

    private Long id;                          // 兼职ID

    private String title;                      // 兼职标题

    private String employer;                   // 招聘方

    private String location;                   // 工作地点

    private String workTime;                   // 工作时间

    private String salaryUnit;                  // 薪资单位

    private String salaryDesc;                  // 薪资说明

    private Integer type;                       // 兼职类型

    private String typeName;                    // 兼职类型名称（前端展示用）

    private String description;                 // 兼职描述

    private String requirements;                // 招聘要求

    private String contactPerson;               // 联系人

    private String contactPhone;                // 联系电话（根据权限脱敏）

    private Long publisherId;                   // 发布者ID

    private String publisherName;                // 发布者名称

    private Integer status;                      // 状态

    private String statusName;                   // 状态名称（前端展示用）

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;          // 发布时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;           // 更新时间

    private Integer viewCount;                   // 浏览次数

    private Integer applicantCount;              // 申请人数

    // 扩展字段：当前用户是否已申请
    private Boolean hasApplied;                   // 是否已申请

    private Boolean isFavorite;             // 当前用户是否收藏
    private Boolean isApplied;              // 当前用户是否申请
    private Boolean isPublisher;            // 当前用户是否发布者

    // 获取脱敏后的联系电话
    public String getDesensitizedPhone() {
        if (contactPhone != null && contactPhone.length() == 11) {
            return contactPhone.substring(0, 3) + "****" + contactPhone.substring(7);
        }
        return contactPhone;
    }

    // 类型名称转换
    public String getTypeName() {
        if (type == null) return null;
        switch (type) {
            case 1: return "校内兼职";
            case 2: return "校外兼职";
            case 3: return "实习";
            default: return "未知类型";
        }
    }

    // 状态名称转换
    public String getStatusName() {
        if (status == null) return null;
        switch (status) {
            case 0: return "已下架";
            case 1: return "招聘中";
            case 2: return "已招满";
            case 3: return "已截止";
            default: return "未知状态";
        }
    }
}