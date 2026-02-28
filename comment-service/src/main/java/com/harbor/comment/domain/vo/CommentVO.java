package com.harbor.comment.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 评论视图对象
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentVO {

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 业务类型
     */
    private Integer targetType;

    /**
     * 业务类型描述
     */
    public String getTargetTypeDesc() {
        if (targetType == null) return "未知";
        return targetType == 0 ? "兼职" : "商品";
    }

    /**
     * 用户信息
     */
    private String userName;
    private String userAvatar;

    /**
     * 被回复用户信息
     */
    private String replyUserId;
    private String replyUserName;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 图片列表
     */
    private List<String> images;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 根评论ID
     */
    private Long rootId;

    /**
     * 评论层级
     */
    private Integer level;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 回复数
     */
    private Integer replyCount;

    /**
     * 当前用户是否点赞
     */
    private Boolean isLiked = false;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 子评论列表（用于楼中楼）
     */
    private List<CommentVO> children;

    /**
     * 内部类：用户信息
     */
    @Data
    public static class UserInfoVO {
        private Long id;
        private String name;
        private String avatar;
    }
}