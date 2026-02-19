package com.harbor.partTime.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("part_time_tag")
public class PartTimeTagPO {
    @TableId(type = IdType.AUTO)
    private Integer id;                     // 标签ID
    private String name;                     // 标签名称
    private Integer type;                     // 标签类型（1-工作类型，2-薪资类型，3-特殊要求等）
    private Integer sort;                     // 排序
}