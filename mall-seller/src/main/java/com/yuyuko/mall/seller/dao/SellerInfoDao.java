package com.yuyuko.mall.seller.dao;

import com.yuyuko.mall.seller.dto.SellerInfoDTO;
import com.yuyuko.mall.seller.dto.SellerShopSimpleInfoDTO;
import com.yuyuko.mall.seller.entity.SellerInfoDO;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-08
 */
@Repository
public interface SellerInfoDao {
    SellerInfoDTO getSellerInfo(Long sellerId);

    SellerShopSimpleInfoDTO getSellerShopInfo(Long sellerId);

}
