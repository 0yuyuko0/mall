package com.yuyuko.mall.order.message;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemPersistMessage {
    private Long id;

    private Long orderId;

    private Long productId;

    private String productName;

    private String productAvatar;

    private Integer count;

    private BigDecimal price;
}
