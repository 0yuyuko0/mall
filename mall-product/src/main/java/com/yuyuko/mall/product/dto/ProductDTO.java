package com.yuyuko.mall.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;

    private Long shopId;

    private Long brandId;

    private Long categoryId;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private String avatar;

    private Integer sales;

    private Integer commentCount;

    private Integer goodCommentCount;
}