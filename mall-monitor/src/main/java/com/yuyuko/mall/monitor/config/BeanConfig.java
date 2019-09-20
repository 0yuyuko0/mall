package com.yuyuko.mall.monitor.config;

import com.yuyuko.mall.monitor.bean.Shit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public Shit shit(){
        return new Shit();
    }
}
