package com.yuyuko.mall.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserHomeInfoDTO {
    @ApiModelProperty(value = "收藏商品数量",position = 1)
    private Integer likeProductCount;

    @ApiModelProperty(value = "收藏店铺数量",position = 2)
    private Integer likeShopCount;

    @ApiModelProperty(value = "浏览历史数量",position = 3)
    private Integer browseHistoryCount;

    @ApiModelProperty(value = "购物车数量",position = 4)
    private Integer cartItemCount;

    @ApiModelProperty(value = "待支付订单数量",position = 5)
    private Integer waitPayCount;

    @ApiModelProperty(value = "待发货订单数量",position = 6)
    private Integer waitSendCount;

    @ApiModelProperty(value = "待收货订单数量",position = 7)
    private Integer waitReceiveCount;

    @ApiModelProperty(value = "待评价订单数量",position = 8)
    private Integer waitRateCount;
}
