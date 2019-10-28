package com.yuyuko.mall.seller.api.impl;

import com.yuyuko.mall.seller.api.SellerInfoRemotingService;
import com.yuyuko.mall.seller.dao.SellerDao;
import com.yuyuko.mall.seller.dto.SellerShopSimpleInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class SellerInfoServiceImpl implements SellerInfoRemotingService {
    @Autowired
    private SellerDao sellerDao;

    @Override
    public SellerShopSimpleInfoDTO getSellerShopInfo(Long sellerId) {
        return sellerDao.getSellerShopInfo(sellerId);
    }
}
