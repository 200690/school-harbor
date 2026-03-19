package com.harbor.utils.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户更新 DTO
 */
@Data
public class UserInfoDTO {

    @NotNull(message = "用户ID不能为空")
    private Long id;

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "手机号不能为空")
    private String phone;

    private String img;

    private int gender;

    private String signature;

    private String email;

    private String createTime;
}