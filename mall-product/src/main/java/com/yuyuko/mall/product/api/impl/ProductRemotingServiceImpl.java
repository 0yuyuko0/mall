package com.yuyuko.mall.product.api.impl;


import com.yuyuko.mall.common.utils.CollectionUtils;
import com.yuyuko.mall.product.api.ProductRemotingService;
import com.yuyuko.mall.product.dao.ProductDao;
import com.yuyuko.mall.product.dto.CartItemProductDTO;
import com.yuyuko.mall.product.entity.ProductDO;
import com.yuyuko.mall.product.manager.ProductManager;
import com.yuyuko.mall.product.param.ProductCreateParam;
import com.yuyuko.mall.stock.api.StockRemotingService;
import com.yuyuko.mall.stock.param.StockCreateParam;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Slf4j
public class ProductRemotingServiceImpl implements ProductRemotingService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductManager productManager;

    @Reference
    private StockRemotingService stockRemotingService;

    @GlobalTransactional
    @Override
    public void createProduct(ProductCreateParam createParam) {
        ProductDO productDO = buildProductDO(createParam);
        productDao.insert(productDO);

        stockRemotingService.createProductStock(new StockCreateParam(createParam.getId(),
                createParam.getStock()));
    }

    private ProductDO buildProductDO(ProductCreateParam createParam) {
        ProductDO productDO = new ProductDO();
        BeanUtils.copyProperties(createParam, productDO);
        return productDO;
    }

    @Override
    public List<CartItemProductDTO> listCartItemProducts(List<Long> productIds) {
        List<CartItemProductDTO> cartItemProductDTOs = productDao.listCartItemProducts(productIds);

        List<Integer> stocks = stockRemotingService.listStocks(productIds);
        CollectionUtils.consume(cartItemProductDTOs, stocks, CartItemProductDTO::setStock);
        return cartItemProductDTOs;
    }

    @Override
    public boolean exist(Long productId) {
        return productManager.getProduct(productId) != null;
    }
}
