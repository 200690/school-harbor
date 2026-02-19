package com.harbor.secondHand.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("second_hand_category")
public class SecondHandCategoryPO {
    @TableId(type = IdType.AUTO)
    private Integer id;          // 分类ID
    private String name;          // 分类名称
    private Integer parentId;     // 父分类ID，0表示顶级
    private Integer level;        // 层级
    private Integer sort;         // 排序
    private Integer status;       // 状态：0-禁用，1-启用
}