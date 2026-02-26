package com.harbor.secondHand.user.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 关注信息视图对象
 */
@Data
@Accessors(chain = true)
public class FollowVO {
    /**
     * 关注记录ID
     */
    private Long followId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 关注状态
     * 0-未关注 1-已关注 2-互关
     */
    private Integer status;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 性别
     */
    private Integer gender;
}
