package com.yuyuko.mall.order.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel
public class OrderCreateParam {
    @Length(max = 16)
    @ApiModelProperty(value = "联系人名字",position = 1)
    private String consigneeName;

    @Length(min = 11, max = 11)
    @ApiModelProperty(value = "联系人电话号码",position = 2)
    private String consigneePhoneNumber;

    @Length(max = 64)
    @ApiModelProperty(value = "送货地址",position = 3)
    private String deliveryAddress;

    @Length(max = 6)
    @ApiModelProperty(value = "支付方式",position = 4)
    private String paymentMethod;

    @NotNull
    @DecimalMax("10000000.00")
    @ApiModelProperty(value = "订单总金额",position = 5)
    private BigDecimal totalPrice;

    @NotNull
    @DecimalMax("100.00")
    @ApiModelProperty(value = "运费",position = 6)
    private BigDecimal deliveryFee;

    @NotNull
    @DecimalMax("10000000.00")
    @ApiModelProperty(value = "实际支付",position = 7)
    private BigDecimal actualPayment;

    @Valid
    @Size(min = 1)
    @ApiModelProperty(value = "各个商家的订单",position = 8)
    private List<ShopOrderCreateParam> shopOrders;
}
