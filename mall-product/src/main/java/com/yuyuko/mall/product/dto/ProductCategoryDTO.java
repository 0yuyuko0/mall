package com.yuyuko.mall.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("productCategory")
public class ProductCategoryDTO {
    @ApiModelProperty(value = "商品类目id", position = 1)
    private Long id;

    @ApiModelProperty(value = "商品类目名", position = 2)
    private String name;

    @ApiModelProperty(value = "商品类目层数", position = 3)
    private Integer level;

    @ApiModelProperty(value = "是否是类目树的叶子类目", position = 4)
    private Boolean isLeaf;
}
