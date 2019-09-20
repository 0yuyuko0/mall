package com.yuyuko.mall.product.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class CartItemProductDTO implements Serializable {
    private Long id;

    private String name;

    private Long stock;

    private BigDecimal price;

    private String avatar;
}