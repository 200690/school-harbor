package com.harbor.comment.domain.vo;

import lombok.Data;

import java.util.Map;

/**
 * 评论统计视图对象
 */
@Data
public class CommentStatVO {

    /**
     * 目标类型
     */
    private Integer targetType;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 总评论数
     */
    private Long totalCount;

    /**
     * 总回复数
     */
    private Long replyCount;

    /**
     * 参与人数
     */
    private Long participantCount;

    /**
     * 评分分布
     */
    private Map<Integer, Long> ratingDistribution;

    /**
     * 好评率
     */
    private Double positiveRate;

    /**
     * 综合评分
     */
    private Double averageRating;
}