package com.harbor.gateway.filter;

import com.harbor.gateway.config.AutoProperties;
import com.harbor.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter , Ordered {
    private final AutoProperties authProperties;
    private final JwtTool jwtTool;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//         1. 从exchange中获取当前的HTTP请求对象
        ServerHttpRequest request = exchange.getRequest();
//         2. 获取请求的路径（比如 /api/auth/login 或 /api/orders/list）
        if(isExclude(request.getPath().toString())){
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
//          全部取消注释TODO
////        获取token
//        String token = null;
//        List<String> headers = request.getHeaders().get("Authorization");
//        if(CollUtil.isNotEmpty(headers)){
//            token = headers.get(0);
//        }
////        令牌校验 TODO
//        Long userId = null;
//        try {
//            userId = jwtTool.parseToken(token);
//        } catch (UnauthorizedException e) {
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
////         传递用户信息
//        String userInfo = userId.toString();
//        ServerWebExchange swe = exchange.mutate().request(builder -> builder.header("user-info", userInfo))
//                .build();
////        System.out.println(userId);
//        return chain.filter(swe);
    }

    private boolean isExclude(String path) {
        for(String pathPattern : authProperties.getExcludePaths())
            if (antPathMatcher.match(pathPattern,path)) {
                return true;
            }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
