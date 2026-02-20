package com.harbor.secondHand.domain.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentCallbackDTO {
    private String orderNo;                // 业务订单号
    private String payOrderId;             // 支付系统订单ID
    private String payOrderNo;              // 支付网关流水号
    private String payChannel;              // 支付渠道
    private Integer payStatus;              // 支付状态
    private BigDecimal amount;              // 支付金额
    private LocalDateTime payTime;          // 支付时间
    private String signature;               // 签名验证
}