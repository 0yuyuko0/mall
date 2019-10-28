package com.yuyuko.mall.order.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class ShopOrderCreateParam {
    @NotNull
    @ApiModelProperty(position = 1)
    private Long shopId;

    @Length(max = 16)
    @ApiModelProperty(position = 2)
    private String shopName;

    @NotNull
    @DecimalMax("10000000.00")
    @ApiModelProperty(position = 3)
    private BigDecimal totalPrice;

    @NotNull
    @DecimalMax("100.00")
    @ApiModelProperty(position = 4)
    private BigDecimal deliveryFee;

    @NotNull
    @DecimalMax("10000000.00")
    @ApiModelProperty(position = 5)
    private BigDecimal actualPayment;

    @Length(max = 6)
    @ApiModelProperty(position = 6)
    private String deliveryMethod;

    @Length(max = 24)
    @ApiModelProperty(position = 7)
    private String timeExpectedDelivery;

    @Valid
    @ApiModelProperty(position = 8)
    private List<OrderItemCreateParam> orderItems;
}