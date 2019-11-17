package com.yuyuko.mall.seller.api;


import com.yuyuko.mall.seller.dto.SellerShopInfoDTO;

import javax.validation.constraints.NotNull;

public interface SellerRemotingService {
    SellerShopInfoDTO getSellerShopInfo(@NotNull Long sellerId);
}
