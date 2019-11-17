package com.yuyuko.mall.stock.api.impl;

import com.yuyuko.mall.stock.api.StockRemotingService;
import com.yuyuko.mall.stock.param.StockCreateParam;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxy;
import org.springframework.stereotype.Component;

@Component
public class GlobalTransactionOriginator {
    @Reference
    private StockRemotingService stockRemotingService;

    @GlobalTransactional
    public void createProductStock() {
        stockRemotingService.createProductStock(new StockCreateParam());
    }

}
