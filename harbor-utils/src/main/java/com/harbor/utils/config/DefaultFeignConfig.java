package com.harbor.utils.config;

import com.harbor.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return template -> {
            Long userId = UserContext.getUser();
            if(userId != null){
                template.header("user-info", userId.toString());
            }
        };
    }
}
