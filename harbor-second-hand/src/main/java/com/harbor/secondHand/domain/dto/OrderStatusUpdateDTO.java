package com.harbor.secondHand.domain.dto;

import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    private Long orderId;
    private Integer status;                // 目标状态
    private String cancelReason;           // 取消原因（取消时填写）
    private String deliveryNo;             // 快递单号（发货时填写）
}