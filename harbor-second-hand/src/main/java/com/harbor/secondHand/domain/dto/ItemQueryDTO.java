package com.harbor.secondHand.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemQueryDTO {
    // 分页参数
    private Integer pageNum = 1;          // 当前页码
    private Integer pageSize = 10;         // 每页条数

    // 查询条件
    private String keyword;                 // 搜索关键词
    private Integer categoryId;              // 分类ID
    private Long sellerId;                   // 卖家ID（查看某个卖家的商品时使用）
    private BigDecimal minPrice;              // 最低价格
    private BigDecimal maxPrice;              // 最高价格
    private Integer condition;                // 成色
    private String school;                    // 所在学校

    // 排序参数
    private String sortField = "publish_time"; // 排序字段：publish_time-发布时间，price-价格，view_count-浏览量
    private String sortOrder = "desc";         // 排序方式：asc-升序，desc-降序

    // 状态筛选（通常列表只展示上架商品，管理员后台可能需要其他状态）
    private Integer status = 1;                // 默认只查询上架的商品
}