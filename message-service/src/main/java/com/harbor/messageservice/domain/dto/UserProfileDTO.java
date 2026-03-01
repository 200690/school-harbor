package com.harbor.messageservice.domain.dto;

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
public class UserProfileDTO {
    private Long id;
    private Long userId;
    private String username;
    private String avatar;
    private String phone;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
