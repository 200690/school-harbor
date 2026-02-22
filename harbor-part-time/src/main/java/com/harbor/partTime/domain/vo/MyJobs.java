package com.harbor.partTime.domain.vo;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 获取我发布的兼职VO
 */
@Data
public class MyJobs {
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
    private String contactPerson;
    private String contactPhone;
    private Integer viewCount;
    private Integer applicantCount;

    private String status;
}