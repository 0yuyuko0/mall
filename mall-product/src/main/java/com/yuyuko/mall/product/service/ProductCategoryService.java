package com.yuyuko.mall.product.service;

import com.yuyuko.mall.product.dao.BrandDao;
import com.yuyuko.mall.product.dao.ProductCategoryDao;
import com.yuyuko.mall.product.dto.BrandDTO;
import com.yuyuko.mall.product.dto.ProductCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-12
 */
@Service
public class ProductCategoryService {
    @Autowired
    ProductCategoryDao productCategoryDao;

    @Autowired
    BrandDao brandDao;

    public List<ProductCategoryDTO> getProductCategory(Long categoryId) {
        return productCategoryDao.getProductCategory(categoryId);
    }

    public List<BrandDTO> getProductCategoryBrands(Long categoryId) {
        return brandDao.getProductCategoryBrands(categoryId);
    }
}
