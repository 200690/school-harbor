package com.harbor.partTime.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("part_time_review")
public class PartTimeReviewPO {
    @TableId(type = IdType.AUTO)
    private Long id;                       // 评价ID
    private Long partTimeId;                // 兼职ID
    private Long fromUserId;                // 评价人ID
    private Long toUserId;                  // 被评价人ID
    private Long applicationId;             // 关联的申请记录ID
    private Integer rating;                  // 评分（1-5分）
    private String content;                  // 评价内容
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;        // 评价时间
}