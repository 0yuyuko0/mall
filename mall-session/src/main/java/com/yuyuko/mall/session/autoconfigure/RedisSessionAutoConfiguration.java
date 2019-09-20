package com.yuyuko.mall.session.autoconfigure;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.data.redis.config.annotation.web.server.RedisWebSessionConfiguration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

import javax.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.List;

@Configuration
@AutoConfigureBefore(SessionAutoConfiguration.class)
public class RedisSessionAutoConfiguration {
    @Bean
    @Qualifier("configureRedisAction")
    public ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    @Qualifier("springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        ParserConfig.getGlobalInstance().addAccept("com.yuyuko.mall.session.pojo");
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        serializer.getFastJsonConfig().setSerializerFeatures(SerializerFeature.WriteClassName);
        return serializer;
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public WebSessionIdResolver webSessionIdResolver(
            @Value("${server.servlet.session.cookie.name}") String cookieName) {
        return new CookieWebSessionIdResolver() {
            private CookieSerializer resolver = new DefaultCookieSerializer();

            @Override
            public List<String> resolveSessionIds(ServerWebExchange exchange) {
                MultiValueMap<String, org.springframework.http.HttpCookie> cookiesMap =
                        exchange.getRequest().getCookies();
                List<HttpCookie> cookies = cookiesMap.get(cookieName);
                if (CollectionUtils.isEmpty(cookies))
                    return new ArrayList<>();
                MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
                httpServletRequest.setCookies(
                        cookies.stream()
                                .map(cookie -> new Cookie(cookie.getName(), cookie.getValue()))
                                .toArray(Cookie[]::new));
                return resolver.readCookieValues(httpServletRequest);
            }
        };
    }
}