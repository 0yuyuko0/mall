package com.yuyuko.mall.cart.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CartItemDO {
    private Long id;

    private Long userId;

    private Long productId;

    private Long shopId;

    private String shopName;
    
    private Integer count;

    private LocalDateTime timeCreate;
}