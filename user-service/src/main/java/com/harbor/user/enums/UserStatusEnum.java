package com.harbor.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
public enum UserStatusEnum {

    FROZEN(0, "已冻结"),
    NORMAL(1, "正常"),
    BLACKLIST(2, "已拉黑");

    @EnumValue
    private final Integer value;
    private final String desc;

    UserStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDescByValue(Integer value) {
        for (UserStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status.getDesc();
            }
        }
        return "";
    }
}