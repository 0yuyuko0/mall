package com.yuyuko.mall.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ShopInfoDTO implements Serializable {
    private Long id;

    private String name;

    private String description;

    private String location;

    private Integer productCount;

    private Integer likeCount;

    private Integer star;

    private Integer productRate;

    private Integer logisticsRate;

    private Integer afterSalesRate;

    private LocalDateTime timeCreate;
}
