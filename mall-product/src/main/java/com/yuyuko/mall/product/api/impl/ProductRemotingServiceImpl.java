package com.yuyuko.mall.product.api.impl;


import com.yuyuko.mall.product.api.ProductRemotingService;
import com.yuyuko.mall.product.dto.CartItemProductDTO;
import com.yuyuko.mall.product.param.ProductCreateParam;
import com.yuyuko.mall.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Slf4j
public class ProductRemotingServiceImpl implements ProductRemotingService {
    @Autowired
    private ProductService productService;

    @Override
    public void createProduct(ProductCreateParam createParam) {
        productService.createProduct(createParam);
    }

    @Override
    public List<CartItemProductDTO> listCartItemProducts(List<Long> productIds) {
        return productService.listCartItemProducts(productIds);
    }

    @Override
    public boolean exist(Long productId) {
        return productService.exist(productId);
    }
}
