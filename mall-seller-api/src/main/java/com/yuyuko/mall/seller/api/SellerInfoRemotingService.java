package com.yuyuko.mall.seller.api;


import com.yuyuko.mall.seller.dto.SellerShopSimpleInfoDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public interface SellerInfoRemotingService {
    SellerShopSimpleInfoDTO getSellerShopInfo(@NotNull Long sellerId);
}
