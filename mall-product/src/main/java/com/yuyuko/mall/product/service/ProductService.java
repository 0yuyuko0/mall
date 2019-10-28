package com.yuyuko.mall.product.service;

import com.yuyuko.mall.common.utils.CollectionUtils;
import com.yuyuko.mall.product.bo.ProductBO;
import com.yuyuko.mall.product.dao.ProductDao;
import com.yuyuko.mall.product.dto.CartItemProductDTO;
import com.yuyuko.mall.product.dto.ProductDTO;
import com.yuyuko.mall.product.entity.ProductDO;
import com.yuyuko.mall.product.manager.ProductManager;
import com.yuyuko.mall.product.param.ProductCreateParam;
import com.yuyuko.mall.shop.dto.ShopInfoDTO;
import com.yuyuko.mall.stock.api.StockRemotingService;
import com.yuyuko.mall.stock.param.StockCreateParam;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import com.yuyuko.mall.shop.api.ShopInfoRemotingService;

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
    private ProductDao productDao;

    @Autowired
    private ProductManager productManager;

    @Reference
    private ShopInfoRemotingService shopService;

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
        ShopInfoDTO shopInfoDTO = shopService.getShopInfo(productDTO.getShopId());

        ProductBO productBO = new ProductBO();
        BeanUtils.copyProperties(productDTO, productBO);
        productBO.setShopInfo(shopInfoDTO);

        return productBO;
    }

    @GlobalTransactional
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

    public List<CartItemProductDTO> listCartItemProducts(List<Long> productIds) {
        List<CartItemProductDTO> cartItemProductDTOs = productDao.listCartItemProducts(productIds);

        List<Integer> stocks = stockRemotingService.listStocks(productIds);
        CollectionUtils.consume(cartItemProductDTOs, stocks, CartItemProductDTO::setStock);
        return cartItemProductDTOs;
    }

    public boolean exist(Long productId) {
        return productManager.getProduct(productId) != null;
    }
}