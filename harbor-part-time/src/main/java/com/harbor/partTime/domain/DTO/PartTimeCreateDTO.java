package com.harbor.partTime.domain.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 创建兼职DTO
 */
@Data
public class PartTimeCreateDTO {

    @NotBlank(message = "兼职标题不能为空")
    @Size(max = 100, message = "兼职标题不能超过100个字符")
    private String title;

    @NotBlank(message = "招聘方不能为空")
    @Size(max = 100, message = "招聘方不能超过100个字符")
    private String employer;

    @NotBlank(message = "工作地点不能为空")
    @Size(max = 100, message = "工作地点不能超过100个字符")
    private String location;

    @NotBlank(message = "工作时间不能为空")
    @Size(max = 100, message = "工作时间不能超过100个字符")
    private String workTime;

    @Size(max = 20, message = "薪资单位不能超过20个字符")
    private String salaryUnit;

    @Size(max = 100, message = "薪资说明不能超过100个字符")
    private String salaryDesc;

    @NotNull(message = "兼职类型不能为空")
    @Min(value = 1, message = "兼职类型值错误")
    @Max(value = 3, message = "兼职类型值错误")
    private Integer type;

    @NotBlank(message = "兼职描述不能为空")
    private String description;

    @NotBlank(message = "招聘要求不能为空")
    private String requirements;

    @NotBlank(message = "联系人不能为空")
    @Size(max = 50, message = "联系人不能超过50个字符")
    private String contactPerson;

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;

    private Integer creditScore = 60;        // 信誉分，默认60
}