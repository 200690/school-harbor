package com.harbor.secondHand.user.domain.dto;

import com.harbor.secondHand.user.domain.po.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
public class UserMessageDTO {
    private Long userId;
    private String username;
    private String avatar;
    private String phone;
    private String email;
    private BigDecimal balance;
    private Integer creditScore;
    private LocalDateTime updateTime;
    private String operationType;

    public static UserMessageDTO userToUserMessageDTO(User user) {
        UserMessageDTO userMessageDTO = new UserMessageDTO();
        userMessageDTO.setUserId(user.getId())
                .setUsername(user.getUsername())
                .setAvatar(user.getImg())
                .setPhone(user.getPhone())
                .setEmail(user.getEmail())
                .setCreditScore(user.getCreditScore())
                .setUpdateTime(user.getUpdateTime());
        return userMessageDTO;
    }
}
