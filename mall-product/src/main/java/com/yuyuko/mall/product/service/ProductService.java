package com.yuyuko.mall.product.service;

import com.yuyuko.mall.product.bo.ProductBO;
import com.yuyuko.mall.product.dto.ProductDTO;
import com.yuyuko.mall.product.manager.ProductManager;
import com.yuyuko.mall.shop.dto.ShopDTO;
import com.yuyuko.mall.stock.api.StockRemotingService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyuko.mall.shop.api.ShopRemotingService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-12
 */
@Service
public class ProductService {
    @Autowired
    private ProductManager productManager;

    @Reference
    private ShopRemotingService shopService;

    @Reference
    private StockRemotingService stockRemotingService;

    public ProductBO getProduct(Long productId) {
        ProductDTO productDTO = getProductDTO(productId);

        return buildProductBO(productDTO);
    }

    private ProductDTO getProductDTO(Long productId) {
        ProductDTO productDTO = productManager.getProduct(productId);
        Integer stock = stockRemotingService.getStock(productDTO.getId());
        productDTO.setStock(stock);
        return productDTO;
    }

    private ProductBO buildProductBO(ProductDTO productDTO) {
        ShopDTO shopDTO = shopService.getShopInfo(productDTO.getShopId());

        ProductBO productBO = new ProductBO();
        BeanUtils.copyProperties(productDTO, productBO);
        productBO.setShopInfo(shopDTO);

        return productBO;
    }
}