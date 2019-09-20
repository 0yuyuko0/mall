package com.yuyuko.mall.product.aspect;

import com.yuyuko.mall.common.exception.WrappedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class WrapExceptionAspect {
    @AfterThrowing(pointcut = "execution(public * com.yuyuko.mall.product.api.impl.*.*(..))",
            throwing="ex")
    public void wrapException(RuntimeException ex) {
        log.error(ex.getMessage());
        throw new WrappedException(ex);
    }
}
