package com.harbor.gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "harbor.auth")
@Slf4j
public class AutoProperties {
    private List<String> includePaths;
    private List<String> excludePaths;

    // ✅ 启动时验证配置加载
    @PostConstruct
    public void logConfig() {
        log.info("✅ Auth 配置加载成功 | 排除路径: {}", excludePaths);
        if (excludePaths.isEmpty()) {
            log.warn("⚠️  excludePaths 为空！请检查 application.yml 配置");
        }
    }
}
