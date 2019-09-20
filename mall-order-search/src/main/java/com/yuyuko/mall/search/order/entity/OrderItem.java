package com.yuyuko.mall.search.order.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private Long id;

    private String name;

    private String avatar;

    private Integer count;

    private BigDecimal price;
}
