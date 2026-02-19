package com.harbor.secondHand.user.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（加密存储）
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
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
     * 状态（0冻结，1正常，2拉黑）
     */
    private Integer status;

    /**
     * 信誉分（初始默认60，取值范围[0,100]）
     */
    @TableField("credit_score")
    private Integer creditScore;

    /**
     * 用户角色（user: 普通用户, admin: 管理员）
     */
    private String role;

    /**
     * 是否删除（0未删除，1已删除）
     */
    @TableLogic
    @TableField("is_delete")
    private Integer isDelete;

    //TODO 需要配置自动填充器才可以使用
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}