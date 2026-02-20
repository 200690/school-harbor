package com.harbor.secondHand.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("second_hand_favorite")
public class FavoritePO {
    @TableId(type = IdType.AUTO)
    private Long id;                      // 收藏ID
    private Long userId;                   // 用户ID
    private Long itemId;                   // 商品ID
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;      // 收藏时间
}