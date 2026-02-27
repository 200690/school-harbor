package com.harbor.comment.domain.dto;

import lombok.Data;

/**
 * 评论查询DTO
 */
@Data
public class CommentQueryDTO {

    /**
     * 业务类型
     */
    private Integer targetType;

    /**
     * 业务ID
     */
    private Long targetId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论状态
     */
    private Integer status;

    /**
     * 排序方式：1-时间倒序，2-点赞数倒序，3-热度倒序
     */
    private Integer sortType = 1;

    /**
     * 是否只查热门评论
     */
    private Boolean onlyHot = false;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}