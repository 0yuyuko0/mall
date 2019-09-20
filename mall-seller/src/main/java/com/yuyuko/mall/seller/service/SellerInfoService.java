package com.yuyuko.mall.seller.service;

import com.yuyuko.mall.seller.dao.SellerInfoDao;
import com.yuyuko.mall.seller.dto.SellerInfoDTO;
import com.yuyuko.mall.seller.dto.SellerShopSimpleInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-08
 */
@Service
public class SellerInfoService {
    @Autowired
    SellerInfoDao sellerInfoDao;

    public SellerInfoDTO getSellerInfo(Long sellerId) {
        return sellerInfoDao.getSellerInfo(sellerId);
    }

    public SellerShopSimpleInfoDTO getSellerShopInfo(Long sellerId){
        return sellerInfoDao.getSellerShopInfo(sellerId);
    }
}