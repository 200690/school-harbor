package com.harbor.user.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户信息 VO - 用于返回给前端
 */
@Data
@Accessors(chain = true)
public class UserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号（脱敏显示）
     */
    private String phone;

    /**
     * 邮箱（脱敏显示）
     */
    private String email;

    /**
     * 头像URL
     */
    private String img;

    /**
     * 性别
     */
    private String gender;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 信誉分
     */
    private Integer creditScore;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    // 不返回密码、isDelete等敏感字段
}