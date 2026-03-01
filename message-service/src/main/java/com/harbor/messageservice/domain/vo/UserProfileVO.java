package com.harbor.messageservice.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class UserProfileVO {
    private Long id;
    private Long userId;
    private String username;
    private String avatar;
    private String phone;
    private LocalDateTime createTime;  // 前端可能需要展示创建时间
    private LocalDateTime updateTime;  // 前端可能需要展示更新时间
}