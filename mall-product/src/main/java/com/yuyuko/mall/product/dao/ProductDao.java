package com.yuyuko.mall.product.dao;

import com.yuyuko.mall.product.dto.CartItemProductDTO;
import com.yuyuko.mall.product.dto.ProductDTO;
import com.yuyuko.mall.product.entity.ProductDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-12
 */
@Mapper
public interface ProductDao {
    ProductDTO getProduct(Long productId);

    List<CartItemProductDTO> listCartItemProducts(List<Long> productIds);

    int insert(ProductDO productDO);
}
