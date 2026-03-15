package com.harbor.secondHand.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderDTO {
    @NotNull(message = "商品ID不能为空")
    private Long itemId;

    @NotNull(message = "卖家ID不能为空")
    private Long sellerId;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @NotNull(message = "总金额不能为空")
    private BigDecimal totalAmount;
}