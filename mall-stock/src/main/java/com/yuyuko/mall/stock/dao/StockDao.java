package com.yuyuko.mall.stock.dao;

import com.yuyuko.mall.stock.dto.StockDTO;
import com.yuyuko.mall.stock.entity.StockDO;
import com.yuyuko.mall.stock.param.StockDeductParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface StockDao {
    Integer getStock(Long productId);

    List<StockDTO> listStocks(List<Long> productIds);

    int insert(StockDO stockDO);

    int deductStock(StockDeductParam deductParam);

    int deleteByProductId(Long id);
}