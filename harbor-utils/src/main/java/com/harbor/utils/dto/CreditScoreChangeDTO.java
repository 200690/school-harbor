package com.harbor.utils.dto;

import lombok.Data;

@Data
public class CreditScoreChangeDTO {
    private Long userId;
    private Integer changeValue;
}