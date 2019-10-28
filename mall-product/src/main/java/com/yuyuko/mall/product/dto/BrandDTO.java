package com.yuyuko.mall.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("brand")
public class BrandDTO {
    @ApiModelProperty(value = "品牌id", position = 1)
    private Long id;

    @ApiModelProperty(value = "品牌名",position = 2)
    private String name;

    @ApiModelProperty(value = "品牌别名",position = 3)
    private String alias;
}
