package com.yuyuko.mall.search.product.entity;

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
@Document(indexName = "#{elasticsearchConfig.index}",type = "#{elasticsearchConfig.type}")
public class Product {
    @Id
    private Long id;

    private Long shopId;

    private String shopName;

    private Long brandId;

    private String brandName;

    private String brandAlias;

    private Long categoryId;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private String avatar;

    private Integer sales;

    private Integer commentCount;

    private Integer goodCommentCount;

    private LocalDateTime timeCreate;
}
