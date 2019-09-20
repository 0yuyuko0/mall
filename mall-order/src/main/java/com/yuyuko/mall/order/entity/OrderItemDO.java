package com.yuyuko.mall.order.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderItemDO implements Serializable {


    private Long id;

    private Long orderId;

    private Long productId;

    private String productName;

    private String productAvatar;

    private Integer count;

    private BigDecimal price;
}
