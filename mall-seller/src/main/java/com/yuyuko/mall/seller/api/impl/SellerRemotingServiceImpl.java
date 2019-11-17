package com.yuyuko.mall.seller.api.impl;

import com.yuyuko.mall.seller.api.SellerRemotingService;
import com.yuyuko.mall.seller.dao.SellerDao;
import com.yuyuko.mall.seller.dto.SellerShopInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class SellerRemotingServiceImpl implements SellerRemotingService {
    @Autowired
    private SellerDao sellerDao;

    @Override
    public SellerShopInfoDTO getSellerShopInfo(Long sellerId) {
        return sellerDao.getSellerShopInfo(sellerId);
    }
}
