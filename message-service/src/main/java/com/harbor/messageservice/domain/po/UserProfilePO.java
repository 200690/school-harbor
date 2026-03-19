package com.harbor.messageservice.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("user_profile")
@Accessors(chain = true)
public class UserProfilePO {
    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID
    private Long userId;             // 用户ID（与用户服务一致）
    private String username;         // 昵称
    private String avatar;           // 头像URL
    private String phone;            // 手机号
    private String email;
    private BigDecimal balance;
    private Integer creditScore;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime; // 更新时间
    private String role = "user";
}