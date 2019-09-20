package com.yuyuko.mall.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderDO implements Serializable {


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


}
