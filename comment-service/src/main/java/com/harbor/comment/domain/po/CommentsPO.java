package com.harbor.comment.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论表持久化对象
 */
@Data
@TableName("comments")
public class CommentsPO {
    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 业务类型: 0-兼职, 1-商品
     */
    private Integer targetType;

    /**
     * 业务ID: 对应业务的ID
     */
    private Long targetId;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 评论时用户名
     */
    private String userName;

    /**
     * 评论时用户头像
     */
    private String userAvatar;

    /**
     * 被回复用户ID
     */
    private Long replyUserId;

    /**
     * 被回复时用户名
     */
    private String replyUserName;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 图片URL列表（JSON数组格式）
     */
    private String images;

    /**
     * 父评论ID，0表示一级评论
     */
    private Long parentId;

    /**
     * 根评论ID，用于聚合所有回复
     */
    private Long rootId;

    /**
     * 评论层级：1-一级评论，2-二级回复
     */
    private Integer level;

    /**
     * 评论路径（用于排序和查询）
     */
    private String path;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 回复数
     */
    private Integer replyCount;

    /**
     * 状态：0-待审核，1-正常，2-隐藏，3-管理员删除，4-违规
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 删除时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime deletedAt;
}
