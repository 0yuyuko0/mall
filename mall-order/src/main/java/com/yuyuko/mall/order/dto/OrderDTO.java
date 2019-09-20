package com.yuyuko.mall.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;

    private Long userId;

    private Long shopId;

    private String shopName;

    private Integer status;

    private String paymentMethod;

    private BigDecimal totalPrice;

    private BigDecimal deliveryFee;

    private BigDecimal actualPayment;

    private LocalDateTime timePay;

    private String deliveryMethod;

    private String timeExpectedDelivery;

    private String consigneeName;

    private String consigneePhoneNumber;

    private String deliveryAddress;

    private LocalDateTime timeCreate;

    private List<OrderItemDTO> orderItems;
}
