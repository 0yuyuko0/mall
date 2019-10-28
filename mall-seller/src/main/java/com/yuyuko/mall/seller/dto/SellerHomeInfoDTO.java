package com.yuyuko.mall.seller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SellerHomeInfoDTO {
    @ApiModelProperty(position = 1)
    private Long shopId;

    @ApiModelProperty(position = 2)
    private String shopName;

    @ApiModelProperty(position = 3)
    private Integer waitPayCount;

    @ApiModelProperty(position = 4)
    private Integer waitSendCount;

    @ApiModelProperty(position = 5)
    private Integer waitRefundCount;

    @ApiModelProperty(position = 6)
    private Integer waitRateCount;
}
