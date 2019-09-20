package com.yuyuko.mall.cart.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ShopCartItemBO {
    private Long shopId;

    private String shopName;
    
    private List<CartItemBO> cartItems;
}