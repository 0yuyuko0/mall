package com.yuyuko.mall.product.api;

import com.yuyuko.mall.product.dto.CartItemProductDTO;
import com.yuyuko.mall.product.param.ProductCreateParam;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface ProductRemotingService {
    void createProduct(@NotNull ProductCreateParam createParam);

    List<CartItemProductDTO> listCartItemProducts(@NotEmpty List<Long> productIds);

    boolean exist(@NotNull Long productId);
}