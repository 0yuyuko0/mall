package com.yuyuko.mall.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("shopInfo")
public class ShopInfoDTO implements Serializable {
    @ApiModelProperty(value = "商店id",position = 1)
    private Long id;

    @ApiModelProperty(value = "商店名称",position = 2)
    private String name;

    @ApiModelProperty(value = "商店头像",position = 3)
    private String avatar;

    @ApiModelProperty(value = "商店描述",position = 3)
    private String description;

    @ApiModelProperty(value = "商店地址，'省市'",position = 4)
    private String location;

    @ApiModelProperty(value = "商品数量",position = 5)
    private Integer productCount;

    @ApiModelProperty(value = "收藏数量",position = 6)
    private Integer likeCount;

    @ApiModelProperty(value = "商店评分，最高5星",position = 7)
    private Integer star;

    @ApiModelProperty(value = "商店的商品评分，最高5分",position = 8)
    private Integer productRate;

    @ApiModelProperty(value = "商店的物流评分，最高5分",position = 9)
    private Integer logisticsRate;

    @ApiModelProperty(value = "商店的售后评分，最高5分",position = 10)
    private Integer afterSalesRate;

    @ApiModelProperty(value = "商店的创建时间",position = 11)
    private LocalDateTime timeCreate;
}
