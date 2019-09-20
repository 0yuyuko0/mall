package com.yuyuko.mall.seller.dto;

import lombok.Data;

@Data
public class SellerInfoDTO {
    private Long shopId;

    private String shopName;

    private Integer waitPayCount;

    private Integer waitSendCount;

    private Integer waitRefundCount;

    private Integer waitRateCount;
}
