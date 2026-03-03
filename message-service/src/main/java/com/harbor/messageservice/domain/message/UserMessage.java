package com.harbor.messageservice.domain.message;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserMessage {
    private Long userId;
    private String username;
    private String avatar;
    private String phone;
    private String email;
    private BigDecimal balance;
    private Integer creditScore;
    private LocalDateTime updateTime;
    private String operationType; // CREATE, UPDATE, DELETE
}
