package com.yuyuko.mall.seller.api.impl;

import com.yuyuko.mall.common.exception.WrappedException;
import com.yuyuko.mall.seller.api.SellerInfoService;
import com.yuyuko.mall.seller.dto.SellerShopSimpleInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class SellerInfoServiceImpl implements SellerInfoService {
    @Autowired
    com.yuyuko.mall.seller.service.SellerInfoService sellerInfoService;

    @Override
    public SellerShopSimpleInfoDTO getSellerShopInfo(Long sellerId) {
        try {
            return sellerInfoService.getSellerShopInfo(sellerId);
        }catch (RuntimeException ex){
            log.error(ex.getMessage());
            throw new WrappedException(ex);
        }
    }
}
