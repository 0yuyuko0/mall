package com.yuyuko.mall.cart.bo;

import com.yuyuko.mall.product.dto.CartItemProductDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("cartItem")
public class CartItemBO {
    @ApiModelProperty("购物车item的id，注意别和商品id弄混了")
    private Long id;

    @ApiModelProperty("商品")
    private CartItemProductDTO product;

    @ApiModelProperty("数量")
    private Integer count;
}
