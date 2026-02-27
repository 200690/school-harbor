package com.harbor.secondHand.user.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BlockUserVO {
    /**
     * 拉黑记录ID
     * 拉黑表记录
     */
    private Long id;
    private Long userId;
    private Long blockId;
    private LocalDateTime blockTime;

    /*
      用户表信息
      blockUser
     */
    private String username;
    private String avatar;
    private String signature;
}
