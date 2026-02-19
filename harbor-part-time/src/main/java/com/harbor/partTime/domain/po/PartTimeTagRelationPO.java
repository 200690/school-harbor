package com.harbor.partTime.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("part_time_tag_relation")
public class PartTimeTagRelationPO {
    @TableId(type = IdType.AUTO)
    private Long id;                       // 关联ID
    private Long partTimeId;                // 兼职ID
    private Integer tagId;                   // 标签ID
}