package com.harbor.secondHand.user.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoginVO {
    private String token;
    private Long userId;
    private String username;
    private Integer balance;
    // 角色身份
    private String role;
}
