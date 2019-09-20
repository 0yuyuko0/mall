package com.yuyuko.mall.cart.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartItemDTO {
    private Long id;

    private Long productId;

    private Long shopId;

    private String shopName;
    
    private Integer count;

    private LocalDateTime timeCreate;
}
