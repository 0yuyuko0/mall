package com.yuyuko.mall.shop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ShopDO implements Serializable {


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
