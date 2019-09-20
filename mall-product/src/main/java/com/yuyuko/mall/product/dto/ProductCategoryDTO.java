package com.yuyuko.mall.product.dto;

import lombok.Data;

@Data
public class ProductCategoryDTO {
    private Long id;

    private String name;

    private Boolean isLeaf;
}
