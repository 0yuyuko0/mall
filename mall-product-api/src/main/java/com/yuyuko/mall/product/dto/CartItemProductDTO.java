package com.yuyuko.mall.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("product")
public class CartItemProductDTO implements Serializable {
    @ApiModelProperty(value = "商品id",position = 1)
    private Long id;

    @ApiModelProperty(value = "商品名字",position = 5)
    private String name;

    @ApiModelProperty(value = "商品价格，精确到小数点后两位",position = 6)
    private BigDecimal price;

    @ApiModelProperty(value = "商品库存",position = 7)
    private Integer stock;

    @ApiModelProperty(value = "商品图片",position = 8)
    private String avatar;
}