package com.yuyuko.mall.cart.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("shopCartItem")
public class ShopCartItemBO {
    @ApiModelProperty(value = "超市id",position = 1)
    private Long shopId;

    @ApiModelProperty(value = "超市名称",position = 2)
    private String shopName;

    @ApiModelProperty(value = "属于该超市的cartItem",position = 3)
    private List<CartItemBO> cartItems;
}