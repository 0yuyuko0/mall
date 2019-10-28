package com.yuyuko.mall.admin.config;

import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.common.message.ProtostuffMessageCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageCodecConfig {
    @Bean
    public MessageCodec messageCodec() {
        return new ProtostuffMessageCodec();
    }
}
