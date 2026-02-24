package com.harbor.partTime.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 兼职详情VO（继承基础VO，可添加额外信息）
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PartTimeDetailVO extends BaseJobStatusVO{
    private Long id;
    private String title;
    private String employer;
    private String location;
    private String workTime;
    private String salaryUnit;
    private String salaryDesc;
    private Integer type;
    private String description;
    private String requirements;
    private Long publisherId;
    private String contactPerson;       // 联系人
    private String contactPhone;        // 联系电话
    private Integer status;
    private Integer viewCount;
    private Integer applicantCount;
    private String username;
    private String img;
}