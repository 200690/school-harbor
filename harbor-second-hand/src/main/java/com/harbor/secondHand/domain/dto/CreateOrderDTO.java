package com.harbor.secondHand.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderDTO {
    @NotNull(message = "商品ID不能为空")
    private Long itemId;

    @NotNull(message = "买家ID不能为空")
    private Long buyerId;

    @NotNull(message = "卖家ID不能为空")
    private Long sellerId;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @NotNull(message = "购买数量不能为空")
    @Positive(message = "购买数量必须大于0")
    private Integer quantity;

    @NotNull(message = "总金额不能为空")
    private BigDecimal totalAmount;

    @NotNull(message = "交易方式不能为空")
    private String tradeMethod;

    private String tradeLocation;          // 面交地点（当面交易时必填）

    private String buyerMessage;           // 买家留言
}