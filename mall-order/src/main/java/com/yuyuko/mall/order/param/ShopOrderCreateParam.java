package com.yuyuko.mall.order.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ShopOrderCreateParam {
    @NotNull
    private Long shopId;

    @Length(max = 16)
    private String shopName;

    @NotNull
    @DecimalMax("10000000.00")
    private BigDecimal totalPrice;

    @NotNull
    @DecimalMax("100.00")
    private BigDecimal deliveryFee;

    @NotNull
    @DecimalMax("10000000.00")
    private BigDecimal actualPayment;

    @Length(max = 6)
    private String deliveryMethod;

    @Length(max = 24)
    private String timeExpectedDelivery;

    @Valid
    private List<OrderItemCreateParam> orderItems;
}