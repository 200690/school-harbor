package com.harbor.messageservice.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class UserItemPostsDTO {
    private Long id;
    private Long itemId;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer syncVersion;
}