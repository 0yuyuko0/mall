package com.yuyuko.mall.product.dao;

import com.yuyuko.mall.product.dto.BrandDTO;
import com.yuyuko.mall.product.entity.BrandDO;
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
public interface BrandDao {
    List<BrandDTO> getProductCategoryBrands(Long categoryId);
}
