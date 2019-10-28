package com.yuyuko.mall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
@ApiModel
public class OrderItemDTO {
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