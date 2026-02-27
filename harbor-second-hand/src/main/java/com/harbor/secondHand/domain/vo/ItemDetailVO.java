package com.harbor.secondHand.domain.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemDetailVO {
    // 商品基本信息
    private Long id;
    private String title;
    private String description;

    // 分类信息
    private Integer categoryId;

    // 价格信息
    private BigDecimal price;
    private BigDecimal originalPrice;

    // 商品属性
    private Integer condition;
    private String school;
    private String location;

    // 图片信息
    private String coverImage;
    private List<String> images;                  // 所有图片列表

    // 卖家信息
    private Long sellerId;
    private String sellerName = "卖家";
    private String sellerAvatar = "";                   // 卖家头像

    // 统计信息
    private Integer viewCount;
    private Integer favoriteCount;
    private LocalDateTime publishTime;
    private LocalDateTime updateTime;

    // 交互状态
//    private Boolean isFavorite;                     // 当前用户是否收藏

    // 关联信息
    private List<ItemListItemVO> relatedItems;      // 相关商品推荐

}