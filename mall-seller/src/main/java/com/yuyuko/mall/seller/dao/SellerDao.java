package com.yuyuko.mall.seller.dao;

import com.yuyuko.mall.seller.dto.SellerHomeInfoDTO;
import com.yuyuko.mall.seller.dto.SellerShopSimpleInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-08
 */
@Mapper
public interface SellerDao {
    SellerHomeInfoDTO getSellerHomeInfo(Long sellerId);

    SellerShopSimpleInfoDTO getSellerShopInfo(Long sellerId);

}
