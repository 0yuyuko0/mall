package com.yuyuko.mall.seller.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SellerShopSimpleInfoDTO implements Serializable {
    private Long shopId;

    private String shopName;
}
