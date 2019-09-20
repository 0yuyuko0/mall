package com.yuyuko.mall.shop.api;


import com.yuyuko.mall.shop.dto.ShopInfoDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public interface ShopInfoService {
    ShopInfoDTO getShopInfo(@NotNull Long shopId);
}
