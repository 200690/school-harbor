package com.harbor.comment.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 评论查询DTO
 */
@Data
public class CommentQueryDTO {

    /**
     * 业务类型, 0-兼职，1-商品，2-全部
     */
    @NotNull
    private Integer targetType;

    /**
     * 业务ID
     */
    private Long targetId;

    /**
     * 用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 排序方式：1-时间倒序，2-点赞数倒序
     */
    private Integer sortType = 1;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    @NotNull
    private Integer type; // 1-发送， 2-接收
}