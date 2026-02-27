package com.harbor.comment.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 点赞DTO
 */
@Data
public class CommentLikeDTO {

    /**
     * 评论ID
     */
    @NotNull(message = "评论ID不能为空")
    private Long commentId;

    /**
     * 点赞状态：0-取消点赞，1-点赞
     */
    @NotNull(message = "点赞状态不能为空")
    private Integer status;
}