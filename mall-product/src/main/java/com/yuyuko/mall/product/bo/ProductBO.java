package com.yuyuko.mall.product.bo;

import com.yuyuko.mall.shop.dto.ShopInfoDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductBO {
    private Long id;

    private ShopInfoDTO shopInfo;

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
