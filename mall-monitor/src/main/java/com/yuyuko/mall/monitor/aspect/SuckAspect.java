package com.yuyuko.mall.monitor.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SuckAspect {
    @Before("execution(public * com.yuyuko.mall.monitor.bean.ISuck.suck(..))")
    public void beforeSuck() {
        System.out.println("before suck");
    }
}
