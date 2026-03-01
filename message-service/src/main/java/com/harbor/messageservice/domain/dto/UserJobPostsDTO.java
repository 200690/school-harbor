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
public class UserJobPostsDTO {
    private Long id;
    private Long jobId;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal salary;
    private String location;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer syncVersion;
}