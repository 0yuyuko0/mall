package com.yuyuko.mall.monitor.bean;

import org.springframework.stereotype.Component;

@Component
public class Suck implements ISuck {
    public void suck(){
        System.out.println("suck");
    }
}
