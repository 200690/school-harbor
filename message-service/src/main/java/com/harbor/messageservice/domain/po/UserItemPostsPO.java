package com.harbor.messageservice.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors
@TableName("user_item_posts")
public class UserItemPostsPO {
    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID
    private Long itemId;             // 二手服务中的原始ID
    private Long userId;             // 发布者ID
    private String title;            // 标题
    private String description;      // 描述
    private BigDecimal price;        // 价格
    private String category;         // 分类
    private Integer status;          // 状态：0-下架 1-在售 2-已售
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime; // 更新时间
    @Version
    private Integer syncVersion;      // 乐观锁版本号
}