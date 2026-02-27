package com.harbor.comment.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 评论列表视图对象
 */
@Data
public class CommentListVO {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 置顶评论列表
     */
    private List<CommentVO> topComments;

    /**
     * 热门评论列表
     */
    private List<CommentVO> hotComments;

    /**
     * 普通评论列表
     */
    private List<CommentVO> comments;

    /**
     * 统计信息
     */
    private StatVO stat;

    /**
     * 内部类：统计信息
     */
    @Data
    public static class StatVO {
        /**
         * 总评论数
         */
        private Long totalCount;

        /**
         * 好评数（评分>3）
         */
        private Long positiveCount;

        /**
         * 中评数（评分=3）
         */
        private Long neutralCount;

        /**
         * 差评数（评分<3）
         */
        private Long negativeCount;

        /**
         * 平均评分
         */
        private Double averageRating;
    }
}