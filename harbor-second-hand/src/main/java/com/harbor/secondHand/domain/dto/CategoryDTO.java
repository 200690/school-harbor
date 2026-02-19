package com.harbor.secondHand.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryDTO {
    private Integer id;
    private String name;
    private Integer parentId;
    private Integer level;
    private Integer sort;
    private Integer status;
    private List<CategoryDTO> children;  // 子分类（树形结构时使用）
}