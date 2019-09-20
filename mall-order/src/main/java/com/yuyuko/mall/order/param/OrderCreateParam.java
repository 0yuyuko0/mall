package com.yuyuko.mall.order.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateParam {
    @Length(max = 16)
    private String consigneeName;

    @Length(min = 11, max = 11)
    private String consigneePhoneNumber;

    @Length(max = 64)
    private String deliveryAddress;

    @Length(max = 6)
    private String paymentMethod;

    @NotNull
    @DecimalMax("10000000.00")
    private BigDecimal totalPrice;

    @NotNull
    @DecimalMax("100.00")
    private BigDecimal deliveryFee;

    @NotNull
    @DecimalMax("10000000.00")
    private BigDecimal actualPayment;

    @Valid
    @Size(min = 1)
    private List<ShopOrderCreateParam> shopOrders;
}
