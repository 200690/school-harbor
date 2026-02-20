package com.harbor.secondHand.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ItemQueryConditionDTO {
    private String keyword;
    private Integer categoryId;         // 分类ID
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer condition;          // 成色列表
    private Integer status = 1;
    private Long currentUserId;                 // 当前用户ID（用于判断是否收藏）

    // 分页参数
    private Integer page = 1;
    private Integer size = 10;

    // 排序参数
    private String sortField;       // 排序字段（发布时间publishTime, 浏览量viewCount, 收藏量favoriteCount, 价格price）
    private String sortOrder;       // 排序方式（asc/desc）
}