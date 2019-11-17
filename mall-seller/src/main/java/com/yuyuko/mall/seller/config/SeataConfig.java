package com.yuyuko.mall.seller.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@DependsOn("druidDataSource")
public class SeataConfig {
    @Bean
    @Primary
    public DataSource dataSourceProxy(@Qualifier("druidDataSource")DruidDataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }

    @Bean
    public GlobalTransactionScanner globalTransactionScanner(
            @Value("${spring.application.name}")
                    String applicationName) {
        return new GlobalTransactionScanner(
                applicationName,
                "seller-service-group");
    }
}