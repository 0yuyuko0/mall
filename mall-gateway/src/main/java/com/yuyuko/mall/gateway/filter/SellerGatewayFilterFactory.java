package com.yuyuko.mall.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Component
public class SellerGatewayFilterFactory extends AbstractGatewayFilterFactory<SellerGatewayFilterFactory.Config> {
    public SellerGatewayFilterFactory(){
        super(Config.class);
    }

    @Value("${session.userInfo.name}")
    String userSessionInfoName;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            WebSession session = exchange.getSession().block();
            UserSessionInfo userSessionInfo = session.getAttribute(userSessionInfoName);
            if(userSessionInfo.getIsSeller())
                return chain.filter(exchange);
            else{
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(
                        JSON.toJSONBytes(CommonResult.accessDenied())
                )));
            }
        };
    }

    public static class Config {
        public Config() {
        }
    }
}
