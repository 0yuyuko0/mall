package com.yuyuko.mall.product.dao;

import com.yuyuko.mall.product.dto.BrandDTO;
import com.yuyuko.mall.product.entity.BrandDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-12
 */
@Mapper
public interface BrandDao {
    List<BrandDTO> listBrandsOfCategory(Long categoryId);

    int insert(BrandDO brand);

    int insertProductCategoryBrandRelation(
            @Param("id") long id,
            @Param("productCategoryId") long productCategoryId,
            @Param("brandId") long brandId
    );
}