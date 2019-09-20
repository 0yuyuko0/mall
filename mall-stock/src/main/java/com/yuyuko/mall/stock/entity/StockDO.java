package com.yuyuko.mall.stock.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StockDO {
    private Long id;

    private Long productId;

    private Integer stock;

    private LocalDateTime timeCreate;
}