package com.yuyuko.mall.search.order.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class OrderItem {
    @ApiModelProperty(position = 1)
    private Long id;

    @ApiModelProperty(position = 2)
    private String name;

    @ApiModelProperty(position = 3)
    private String avatar;

    @ApiModelProperty(position = 4)
    private Integer count;

    @ApiModelProperty(position = 5)
    private BigDecimal price;
}
