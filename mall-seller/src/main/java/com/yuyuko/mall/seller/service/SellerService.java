package com.yuyuko.mall.seller.service;

import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.seller.dao.SellerDao;
import com.yuyuko.mall.seller.dto.SellerHomeInfoDTO;
import com.yuyuko.mall.seller.entity.SellerInfoDO;
import com.yuyuko.mall.shop.api.ShopRemotingService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-08
 */
@Service
public class SellerService {
    @Autowired
    private SellerDao sellerDao;

    @Reference
    private ShopRemotingService shopRemotingService;

    @Autowired
    private IdGenerator idGenerator;

    public SellerHomeInfoDTO getSellerHomeInfo(Long sellerId) {
        return sellerDao.getSellerHomeInfo(sellerId);
    }

    @GlobalTransactional
    public SellerHomeInfoDTO registerSeller(Long userId) {
        String shopName = randomShopName();
        Long shopId = shopRemotingService.createShop(shopName);
        SellerInfoDO sellerInfoDO = buildSellerInfoDO(userId, shopId, shopName);
        sellerDao.insert(sellerInfoDO);
        return trans(sellerInfoDO);
    }

    private SellerHomeInfoDTO trans(SellerInfoDO sellerInfoDO) {
        SellerHomeInfoDTO sellerHomeInfoDTO = new SellerHomeInfoDTO();
        sellerHomeInfoDTO.setShopId(sellerInfoDO.getShopId());
        sellerHomeInfoDTO.setShopName(sellerInfoDO.getShopName());
        sellerHomeInfoDTO.setWaitPayCount(0);
        sellerHomeInfoDTO.setWaitRateCount(0);
        sellerHomeInfoDTO.setWaitRefundCount(0);
        sellerHomeInfoDTO.setWaitSendCount(0);
        return sellerHomeInfoDTO;
    }

    private SellerInfoDO buildSellerInfoDO(Long userId, Long shopId, String shopName) {
        SellerInfoDO sellerInfoDO = new SellerInfoDO();
        sellerInfoDO.setId(idGenerator.nextId());
        sellerInfoDO.setUserId(userId);
        sellerInfoDO.setShopId(shopId);
        sellerInfoDO.setShopName(shopName);
        return sellerInfoDO;
    }

    private String randomShopName() {
        return UUID.randomUUID().toString().substring(0, 16);
    }
}