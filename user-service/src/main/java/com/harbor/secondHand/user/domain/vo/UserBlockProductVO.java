package com.harbor.secondHand.user.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用户拉黑商品视图对象
 */
@Data
@Accessors(chain = true)
public class UserBlockProductVO {

    /**
     * 拉黑记录ID
     */
    private Long id;

    /**
     * 用户信息
     */
    private UserVO user;

    /**
     * 拉黑时间
     */
    private LocalDateTime blockTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}