package com.yuyuko.mall.seller.dao;

import com.yuyuko.mall.seller.dto.SellerHomeInfoDTO;
import com.yuyuko.mall.seller.dto.SellerShopInfoDTO;
import com.yuyuko.mall.seller.entity.SellerInfoDO;
import org.apache.ibatis.annotations.Mapper;

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
    int insert(SellerInfoDO sellerInfoDO);

    SellerHomeInfoDTO getSellerHomeInfo(Long sellerId);

    SellerShopInfoDTO getSellerShopInfo(Long sellerId);

}
