package com.harbor.user.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 用户性别枚举
 */
@Getter
public enum UserGender implements IEnum<Integer> {

    MALE(1, "男"),
    FEMALE(0, "女"),
    UNKNOWN(2, "保密");

    /**
     * 性别代码
     */
    private final Integer code;

    /**
     * 性别描述
     */
    private final String desc;

    /**
     * 构造函数
     */
    UserGender(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * MyBatis-Plus会调用这个方法获取存入数据库的值
     */
    @Override
    public Integer getValue() {
        return this.code;
    }
}