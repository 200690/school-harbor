package com.harbor.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRoleEnum {

    USER("user", "普通用户"),
    ADMIN("admin", "管理员");

    @EnumValue
    private final String value;
    private final String desc;

    UserRoleEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDescByValue(String value) {
        for (UserRoleEnum role : values()) {
            if (role.getValue().equals(value)) {
                return role.getDesc();
            }
        }
        return "";
    }
}