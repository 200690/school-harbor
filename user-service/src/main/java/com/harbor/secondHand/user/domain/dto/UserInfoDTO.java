package com.harbor.secondHand.user.domain.dto;

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

    private String img;

    private int gender;

    private String signature;

    @Email(message = "邮箱格式不正确")
    private String email;
}