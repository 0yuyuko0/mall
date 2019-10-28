package com.yuyuko.mall.product.bo;

import com.yuyuko.mall.shop.dto.ShopInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("product")
public class ProductBO {
    @ApiModelProperty(value = "商品id",position = 1)
    private Long id;

    @ApiModelProperty(value = "商品所属商店的简易信息",position = 2)
    private ShopInfoDTO shopInfo;

    @ApiModelProperty(value = "商品品牌id",position = 3)
    private Long brandId;

    @ApiModelProperty(value = "商品类目id",position = 4)
    private Long categoryId;

    @ApiModelProperty(value = "商品名字",position = 5)
    private String name;

    @ApiModelProperty(value = "商品价格，精确到小数点后两位",position = 6)
    private BigDecimal price;

    @ApiModelProperty(value = "商品库存",position = 7)
    private Integer stock;

    @ApiModelProperty(value = "商品图片",position = 8)
    private String avatar;

    @ApiModelProperty(value = "商品销量",position = 9)
    private Integer sales;

    @ApiModelProperty(value = "商品评论数",position = 10)
    private Integer commentCount;

    @ApiModelProperty(value = "商品好评数",position = 11)
    private Integer goodCommentCount;
}
