package com.harbor.secondHand.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("second_hand_review")
public class ReviewPO {
    @TableId(type = IdType.AUTO)
    private Long id;                      // 评价ID
    private Long orderId;                  // 订单ID
    private Long fromUserId;                // 评价人ID
    private Long toUserId;                  // 被评价人ID
    private Integer rating;                 // 评分（1-5）
    private String content;                 // 评价内容
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;       // 创建时间
}