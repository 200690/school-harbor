package com.harbor.secondHand.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@TableName("second_hand_browse_history")
@Accessors(chain = true)
public class BrowseHistoryPO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;                      // 浏览记录ID
    private Long userId;                   // 用户ID
    private Long itemId;                   // 商品ID
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime browseTime;      // 浏览时间
}