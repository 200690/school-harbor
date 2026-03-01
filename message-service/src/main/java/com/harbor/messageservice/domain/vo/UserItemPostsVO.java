package com.harbor.messageservice.domain.vo;

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
public class UserItemPostsVO {
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
    // 可选：syncVersion 通常不返回给前端
    private Integer syncVersion;
}