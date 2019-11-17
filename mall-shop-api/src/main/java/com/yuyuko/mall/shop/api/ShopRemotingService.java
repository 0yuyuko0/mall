package com.yuyuko.mall.shop.api;


import com.yuyuko.mall.shop.dto.ShopDTO;

import javax.validation.constraints.NotNull;

public interface ShopRemotingService {
    ShopDTO getShopInfo(@NotNull Long shopId);

    Long createShop(@NotNull String shopName);
}
