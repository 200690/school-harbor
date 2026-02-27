package com.harbor.comment.enums;

/**
 * 评论状态枚举
 */
public enum CommentStatusEnum {
    PENDING(0, "待审核"),
    NORMAL(1, "正常"),
    HIDDEN(2, "隐藏"),
    DELETED_BY_ADMIN(3, "管理员删除"),
    VIOLATION(4, "违规");

    private final Integer code;
    private final String desc;

    CommentStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() { return code; }
    public String getDesc() { return desc; }
}