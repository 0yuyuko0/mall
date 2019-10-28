package com.yuyuko.mall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel
public class OrderDTO {
    @ApiModelProperty(position = 1)
    private Long id;

    @ApiModelProperty(position = 2)
    private Long userId;

    @ApiModelProperty(position = 3)
    private Long shopId;

    @ApiModelProperty(position = 4)
    private String shopName;

    @ApiModelProperty(position = 5)
    private Integer status;

    @ApiModelProperty(position = 6)
    private String paymentMethod;

    @ApiModelProperty(position = 7)
    private BigDecimal totalPrice;

    @ApiModelProperty(position = 8)
    private BigDecimal deliveryFee;

    @ApiModelProperty(position = 9)
    private BigDecimal actualPayment;

    @ApiModelProperty(position = 10)
    private LocalDateTime timePay;

    @ApiModelProperty(position = 11)
    private String deliveryMethod;

    @ApiModelProperty(position = 12)
    private String timeExpectedDelivery;

    @ApiModelProperty(position = 13)
    private String consigneeName;

    @ApiModelProperty(position = 14)
    private String consigneePhoneNumber;

    @ApiModelProperty(position = 15)
    private String deliveryAddress;

    @ApiModelProperty(position = 16)
    private LocalDateTime timeCreate;

    @ApiModelProperty(position = 17)
    private List<OrderItemDTO> orderItems;
}
