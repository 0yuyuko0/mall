package com.yuyuko.mall.order.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemCreateParam {
    private Long id;

    private String name;

    private String avatar;

    private Integer count;

    private BigDecimal price;
}
