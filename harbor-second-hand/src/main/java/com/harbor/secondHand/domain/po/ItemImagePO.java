package com.harbor.secondHand.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("second_hand_item_image")
public class ItemImagePO {
    @TableId(type = IdType.AUTO)
    private Long id;                      // 图片ID
    private Long itemId;                   // 商品ID
    private String imageUrl;                // 图片URL
    private Integer sortOrder;              // 排序，越小越靠前
    private Integer isCover;                // 是否封面图 0-否 1-是
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;       // 创建时间
}