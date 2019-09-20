package com.yuyuko.mall.cart.dao;

import com.yuyuko.mall.cart.dto.CartItemDTO;
import com.yuyuko.mall.cart.entity.CartItemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao {
    List<CartItemDTO> getCart(Long userId);

    CartItemDTO getCartItem(@Param("userId") Long userId, @Param("productId") Long productId);

    int insert(CartItemDO cartItemDO);

    int deleteById(Long id);

    int updateCartItemCount(@Param("id") Long id, @Param("count") Integer count);
}
