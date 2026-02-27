package com.harbor.comment.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 举报视图对象
 */
@Data
public class CommentReportVO {

    /**
     * 举报ID
     */
    private Long id;

    /**
     * 被举报评论信息
     */
    private CommentVO comment;

    /**
     * 举报人信息
     */
    private UserInfoVO reporter;

    /**
     * 举报类型
     */
    private Integer reasonType;

    /**
     * 举报类型描述
     */
    public String getReasonTypeDesc() {
        switch (reasonType) {
            case 1: return "广告";
            case 2: return "色情";
            case 3: return "辱骂";
            case 4: return "虚假";
            case 5: return "其他";
            default: return "未知";
        }
    }

    /**
     * 具体原因
     */
    private String reasonDesc;

    /**
     * 处理状态
     */
    private Integer status;

    /**
     * 处理状态描述
     */
    public String getStatusDesc() {
        switch (status) {
            case 0: return "待处理";
            case 1: return "已处理（违规）";
            case 2: return "已处理（正常）";
            default: return "未知";
        }
    }

    /**
     * 处理人信息
     */
    private UserInfoVO handler;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 举报时间
     */
    private LocalDateTime createdAt;

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