package com.yuyuko.mall.product.dao;

import com.yuyuko.mall.product.dto.ProductCategoryDTO;
import com.yuyuko.mall.product.entity.ProductCategoryDO;
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
public interface ProductCategoryDao  {
    List<ProductCategoryDTO> listChildCategories(Long parentId);

    int insert(ProductCategoryDO productCategoryDO);
}
