package com.harbor.common.config;

import com.harbor.common.interceptors.UserInfoInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@ConditionalOnClass(DispatcherServlet.class)
@Slf4j
@AutoConfiguration
public class MvcConfig implements WebMvcConfigurer {
    public MvcConfig() {
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor());
    }
}
