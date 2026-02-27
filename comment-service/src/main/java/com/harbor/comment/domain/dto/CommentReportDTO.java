package com.harbor.comment.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 举报DTO
 */
@Data
public class CommentReportDTO {

    /**
     * 评论ID
     */
    @NotNull(message = "评论ID不能为空")
    private Long commentId;

    /**
     * 举报类型
     */
    @NotNull(message = "举报类型不能为空")
    private Integer reasonType;

    /**
     * 具体原因描述
     */
    @Size(max = 200, message = "原因描述不能超过200字")
    private String reasonDesc;
}