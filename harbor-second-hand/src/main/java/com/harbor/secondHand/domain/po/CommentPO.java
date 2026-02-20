package com.harbor.secondHand.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("second_hand_comment")
public class CommentPO {
    @TableId(type = IdType.AUTO)
    private Long id;                      // 留言ID
    private Long itemId;                   // 商品ID
    private Long fromUserId;                // 发送方用户ID
    private Long toUserId;                  // 接收方用户ID
    private String content;                 // 留言内容
    private Long parentId;                  // 父留言ID，用于回复
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;       // 创建时间
}