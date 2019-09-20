package com.yuyuko.mall.cart.bo;

import com.yuyuko.mall.product.dto.CartItemProductDTO;
import lombok.Data;

@Data
public class CartItemBO {
    private Long id;
    
    private CartItemProductDTO product;
    
    private Integer count;
}
