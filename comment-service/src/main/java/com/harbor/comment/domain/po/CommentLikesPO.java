package com.harbor.comment.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 评论点赞表持久化对象
 */
@Data
@TableName("comment_likes")
@Accessors(chain = true)
public class CommentLikesPO {
    /**
     * 点赞ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 点赞用户ID
     */
    private Long userId;

    /**
     * 状态：0-取消点赞，1-点赞
     */
    private Integer status;

    /**
     * 点赞时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
