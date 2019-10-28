package com.yuyuko.mall.search.product.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "#{elasticsearchConfig.index}", type = "#{elasticsearchConfig.type}")
@ApiModel
public class Product {
    @Id
    @ApiModelProperty(position = 1)
    private Long id;

    @ApiModelProperty(position = 2)
    private Long shopId;

    @ApiModelProperty(position = 3)
    private String shopName;

    @ApiModelProperty(position = 4)
    private Long brandId;

    @ApiModelProperty(position = 5)
    private String brandName;

    @ApiModelProperty(position = 6)
    private String brandAlias;

    @ApiModelProperty(position = 7)
    private Long categoryId;

    @ApiModelProperty(position = 8)
    private String name;

    @ApiModelProperty(position = 9)
    private BigDecimal price;

    @ApiModelProperty(position = 10)
    private Integer stock;

    @ApiModelProperty(position = 11)
    private String avatar;

    @ApiModelProperty(position = 12)
    private Integer sales;

    @ApiModelProperty(position = 13)
    private Integer commentCount;

    @ApiModelProperty(position = 14)
    private Integer goodCommentCount;

    @ApiModelProperty(position = 16)
    private LocalDateTime timeCreate;
}
