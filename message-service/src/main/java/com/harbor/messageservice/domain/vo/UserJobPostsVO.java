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
public class UserJobPostsVO {
    private Long id;
    private Long jobId;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal salary;
    private String location;
    private Integer status;
    private LocalDateTime createTime;  // 前端可能展示发布时间
    private LocalDateTime updateTime;  // 前端可能展示更新时间
    // 通常 VO 可以省略 sync_version（乐观锁版本号）因为前端不需要
    // 但为了完整性保留，可根据实际需求决定是否删除
    private Integer syncVersion;
}