package com.harbor.secondHand.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SecondHandUpdateDTO {
    private String title;
    private BigDecimal price;
    private String category;
    private String condition;
    private String location;
    private String description;
    private String image;
}