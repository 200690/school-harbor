package com.harbor.partTime.domain.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 更新兼职DTO
 */
@Data
public class PartTimeUpdateDTO {

    @NotNull(message = "兼职ID不能为空")
    private Long id;

    @Size(max = 100, message = "兼职标题不能超过100个字符")
    private String title;

    @Size(max = 100, message = "招聘方不能超过100个字符")
    private String employer;

    @Size(max = 100, message = "工作地点不能超过100个字符")
    private String location;

    @Size(max = 100, message = "工作时间不能超过100个字符")
    private String workTime;

    @Size(max = 20, message = "薪资单位不能超过20个字符")
    private String salaryUnit;

    @Size(max = 100, message = "薪资说明不能超过100个字符")
    private String salaryDesc;

    @Min(value = 1, message = "兼职类型值错误")
    @Max(value = 3, message = "兼职类型值错误")
    private Integer type;

    private String description;

    private String requirements;

    @Size(max = 50, message = "联系人不能超过50个字符")
    private String contactPerson;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;

    @Min(value = 0, message = "状态值错误")
    @Max(value = 3, message = "状态值错误")
    private Integer status;

    @Min(value = 0, message = "信誉分范围为0-100")
    @Max(value = 100, message = "信誉分范围为0-100")
    private Integer creditScore;
}