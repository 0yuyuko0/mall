package com.yuyuko.mall.order.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(of = "id")
@Accessors(chain = true)
public class OrderItemDTO {
    private Long id;

    private String name;

    private String avatar;

    private Integer count;

    private BigDecimal price;
}