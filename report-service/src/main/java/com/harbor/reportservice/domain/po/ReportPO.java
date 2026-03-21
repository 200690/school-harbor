package com.harbor.reportservice.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

/**
 * 举报主表持久化对象
 */
@Data
@Accessors(chain = true)
@TableName("reports")
public class ReportPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 举报类型：1-评论，2-商品，3-兼职，4-用户
     */
    private Integer reportType;

    /**
     * 被举报对象ID
     */
    private Long targetId;

    /**
     * 举报人ID
     */
    private Long reporterId;

    /**
     * 举报原因类型
     */
    private Integer reasonType;

    /**
     * 具体原因描述
     */
    private String reasonDesc;

    /**
     * 证据图片URL（逗号分隔）
     */
    private String evidenceImages;

    /**
     * 状态：0-待处理，1-已处理（违规），2-已处理（正常）
     */
    private Integer status;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}