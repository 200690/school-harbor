package com.harbor.comment.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论举报表持久化对象
 */
@Data
@TableName("comment_reports")
public class CommentReportsPO {
    /**
     * 举报ID
     */
    private Long id;

    /**
     * 被举报评论ID
     */
    private Long commentId;

    /**
     * 举报人ID
     */
    private Long reporterId;

    /**
     * 举报类型：1-广告，2-色情，3-辱骂，4-虚假，5-其他
     */
    private Integer reasonType;

    /**
     * 具体原因描述
     */
    private String reasonDesc;

    /**
     * 处理状态：0-待处理，1-已处理（违规），2-已处理（正常）
     */
    private Integer status;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime handleTime;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 举报时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
