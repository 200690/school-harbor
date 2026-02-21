package com.harbor.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器
 * 处理插入和更新时的自动填充字段
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    public MyMetaObjectHandler() {
    }

    /**
     * 插入时的填充策略
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 注意：这里的方法名是 setFieldValByName，不是 setValue

        // 创建时间（LocalDateTime类型）
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "publishTime", LocalDateTime.class, LocalDateTime.now());

        // 更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

//        浏览时间
        this.strictInsertFill(metaObject, "browseTime", LocalDateTime.class, LocalDateTime.now());

        // 其他通用字段
        this.strictInsertFill(metaObject, "isDelete", Integer.class, 0);
        this.strictInsertFill(metaObject, "viewCount", Integer.class, 0);
        this.strictInsertFill(metaObject, "favoriteCount", Integer.class, 0);
        this.strictInsertFill(metaObject, "status", Integer.class, 1);
    }

    /**
     * 更新时的填充策略
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}