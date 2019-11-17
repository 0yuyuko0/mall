package com.yuyuko.mall.shop.api.impl;

import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.shop.api.ShopRemotingService;
import com.yuyuko.mall.shop.dao.ShopDao;
import com.yuyuko.mall.shop.dto.ShopDTO;
import com.yuyuko.mall.shop.entity.ShopDO;
import com.yuyuko.mall.shop.manager.ShopManager;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;


@Service
@Slf4j
public class ShopRemotingServiceImpl implements ShopRemotingService {
    @Autowired
    private ShopManager shopManager;

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private IdGenerator idGenerator;

    public ShopDTO getShopInfo(Long shopId) {
        return shopManager.getShop(shopId);
    }

    @Override
    public Long createShop(@NotNull String shopName) {
        ShopDO shopDO = buildShopDO(shopName);
        shopDao.insertShop(shopDO);
        return shopDO.getId();
    }

    private ShopDO buildShopDO(String shopName) {
        ShopDO shopDO = new ShopDO();
        shopDO.setId(idGenerator.nextId());
        shopDO.setName(shopName);
        return shopDO;
    }
}
