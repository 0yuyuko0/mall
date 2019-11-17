package com.yuyuko.mall.search.order;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableSwagger2Doc
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class OrderSearchApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(OrderSearchApplication.class, args);
    }

}
