package com.yuyuko.mall.user.aspect;

import com.yuyuko.mall.common.result.CommonResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Aspect
@Component
public class BindingResultAspect {
    @Pointcut("execution(public * com.yuyuko.mall.user.controller.*.*(..))")
    public void controllerPointCut(){}

    @Pointcut("execution(public * com.yuyuko.mall.user.api.provider.*.*(..))")
    public void providerPointCut(){}



    @Around("controllerPointCut() || providerPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult result = (BindingResult) arg;
                if (result.hasErrors()) {
                    FieldError fieldError = result.getFieldError();
                    if (fieldError != null) {
                        return CommonResult.dataBindingFailed()
                                .message(fieldError.getField() + fieldError.getDefaultMessage());
                    } else {
                        return CommonResult.dataBindingFailed();
                    }
                }
            }
        }
        return joinPoint.proceed();
    }
}
