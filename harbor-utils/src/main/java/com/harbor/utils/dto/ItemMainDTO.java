package com.harbor.utils.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ItemMainDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String coverImage;
    private String school;
    private String location;
    private Integer condition;
    private Integer viewCount;
    private Integer favoriteCount;
}
