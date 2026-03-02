package com.harbor.ossservice.config;

import com.harbor.ossservice.properties.AliOssProperties;
import com.harbor.ossservice.utils.AliOssUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties AliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象：{}", AliOssProperties);
        return new AliOssUtil(
                AliOssProperties.getEndpoint(),
                AliOssProperties.getAccessKeyId(),
                AliOssProperties.getAccessKeySecret(),
                AliOssProperties.getBucketName());
    }
}
