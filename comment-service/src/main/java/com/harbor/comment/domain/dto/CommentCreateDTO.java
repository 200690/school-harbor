package com.harbor.comment.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 创建评论DTO
 */
@Data
public class CommentCreateDTO {

    /**
     * 业务类型: 0-兼职, 1-商品
     */
    @NotNull(message = "业务类型不能为空")
    private Integer targetType;

    /**
     * 业务ID
     */
    @NotNull(message = "业务ID不能为空")
    private Long targetId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 500, message = "评论内容不能超过500字")
    private String content;

    /**
     * 图片URL列表
     */
    private List<String> images;

    /**
     * 父评论ID（回复时必填）
     */
    private Long parentId;

    /**
     * 被回复用户ID（回复时必填）
     */
    private Long replyUserId;

    /**
     * 是否匿名：0-否，1-是
     */
    private Integer isAnonymous = 0;
}