package com.yuyuko.mall.product.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProductDO implements Serializable {


    private Long id;

    private Long sellerId;

    private Long shopId;

    private Long brandId;

    private Long categoryId;

    private String name;

    private BigDecimal price;

    private String avatar;

    private Integer sales;

    private Integer commentCount;

    private Integer goodCommentCount;

    private LocalDateTime timeCreate;


}
