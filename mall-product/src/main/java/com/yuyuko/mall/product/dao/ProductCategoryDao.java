package com.yuyuko.mall.product.dao;

import com.yuyuko.mall.product.dto.ProductCategoryDTO;
import com.yuyuko.mall.product.entity.ProductCategoryDO;
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
@Repository
public interface ProductCategoryDao  {
    List<ProductCategoryDTO> getProductCategory(Long categoryId);
}
