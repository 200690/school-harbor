package com.harbor.secondHand.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderQueryDTO {
    private Long userId;                    // 当前用户ID
    private Integer role;                   // 角色：1-买家，2-卖家
    private Integer status;                  // 订单状态筛选
    private Integer payStatus;               // 支付状态筛选
    private String keyword;                  // 搜索关键词（订单号、商品名）
    private LocalDateTime startTime;         // 开始时间
    private LocalDateTime endTime;           // 结束时间
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}