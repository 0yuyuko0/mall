package com.yuyuko.mall.order.config;

import com.yuyuko.mall.common.utils.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeIdGeneratorConfig {
    @Bean
    public SnowflakeIdGenerator idGenerator(@Value("${snowflake.dataCenterId:0}") int dataCenterId,
                                            @Value("${snowflake.workerId:0}") int workerId
                                            ) {
        return new SnowflakeIdGenerator(dataCenterId, workerId);
    }
}
