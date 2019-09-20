package com.yuyuko.mall.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.yuyuko.mall.common.result.CommonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {
    @Value("${session.cookie.name}")
    String cookieName;

    public AuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpCookie cookie = exchange.getRequest().getCookies().getFirst(cookieName);
            if (cookie == null || !exchange.getSession().block().isStarted()) {
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(
                        JSON.toJSONBytes(CommonResult.unauthorized())
                )));
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        public Config() {
        }
    }
}
