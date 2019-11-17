package com.yuyuko.mall.seller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerShopInfoDTO implements Serializable {
    private Long shopId;

    private String shopName;
}
