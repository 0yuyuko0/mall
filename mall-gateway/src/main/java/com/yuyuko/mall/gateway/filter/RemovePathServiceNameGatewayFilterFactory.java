package com.yuyuko.mall.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

@Component
public class RemovePathServiceNameGatewayFilterFactory extends AbstractGatewayFilterFactory<RemovePathServiceNameGatewayFilterFactory.Config> {

    public RemovePathServiceNameGatewayFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        String replacement = config.replacement.replace("$\\", "$");
        return (exchange, chain) -> {
            ServerHttpRequest req = exchange.getRequest();
            addOriginalRequestUrl(exchange, req.getURI());
            String path = req.getURI().getRawPath();
            String newPath = path.replaceAll(config.regexp, replacement);

            ServerHttpRequest request = req.mutate().path(newPath).build();

            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    public static class Config {
        String serviceName;

        String regexp;

        private final String replacement = "/${remaining}";

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
            this.regexp = String.format("/%s/(?<remaining>.*)", serviceName);
        }
    }
}
