package com.yuyuko.mall.stock.service;

import com.yuyuko.mall.stock.exception.StockDeductRollbackException;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.param.StockDeductParam;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockServiceTests {

    @Autowired
    StockService stockService;

    @Test
    public void getStock() {
        stockService.getStock(65L);
    }

    @Test
    public void test() throws StockDeductRollbackException, StockNotEnoughException {
        stockService.deductStock(List.of(
                new StockDeductParam(60L, 5),
                new StockDeductParam(61L, 20)));
    }

    @Test
    public void listStocks() {
        stockService.listStocks(List.of(65L, 66L));
    }
}