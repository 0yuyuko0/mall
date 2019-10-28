package com.yuyuko.mall.seller.service;

import com.yuyuko.mall.seller.dao.SellerDao;
import com.yuyuko.mall.seller.dto.SellerHomeInfoDTO;
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
    private SellerDao sellerDao;

    public SellerHomeInfoDTO getSellerHomeInfo(Long sellerId) {
        return sellerDao.getSellerHomeInfo(sellerId);
    }
}