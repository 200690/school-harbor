package com.harbor.partTime.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("part_time_favorite")
public class PartTimeFavoritePO {
    @TableId(type = IdType.AUTO)
    private Long id;                       // 收藏ID
    private Long userId;                    // 用户ID
    private Long partTimeId;                // 兼职ID
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;       // 收藏时间
}