package com.harbor.secondHand.domain.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SecondHandItemListItemVO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String coverImage;
    private String conditionDesc;        // 成色描述（如“95新”）
    private String school;                // 所在学校
    private Integer viewCount;
    private Integer favoriteCount;
    private LocalDateTime publishTime;
    private Long sellerId;
    private String sellerName;            // 卖家昵称（从用户服务获取）
    private String sellerAvatar;          // 卖家头像
    private Boolean isFavorite;            // 当前用户是否收藏
}