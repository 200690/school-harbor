package com.harbor.secondHand.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyItem {
    private Long id;
    private String title;
    private String description;
    private String categoryId;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer condition;
    private String coverImage;
    private String school;
    private String location;
    private Integer viewCount;
    private Integer applicantCount;

    private int status;

    private Long buyerId;
    private Long orderId;
}