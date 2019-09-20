package com.yuyuko.mall.order.message;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderItemCreateMessage {
    @NotNull
    private Long id;

    @Length(max = 64)
    private String name;

    @Length(max = 255)
    private String avatar;

    @Max(1000)
    private Integer count;

    @DecimalMax("10000000.00")
    private BigDecimal price;
}
