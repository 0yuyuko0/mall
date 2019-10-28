package com.yuyuko.mall.product.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class ProductCategoryDO implements Serializable {
    private Long id;

    private String name;

    private Long parentId;

    private Integer level;

    private Boolean isLeaf;

    private LocalDateTime timeCreate;
}
