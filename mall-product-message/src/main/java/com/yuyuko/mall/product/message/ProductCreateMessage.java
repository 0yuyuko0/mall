package com.yuyuko.mall.product.message;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductCreateMessage {
    @NotNull
    private Long id;

    @NotNull
    private Long shopId;

    @Length(max = 16)
    private String shopName;

    @NotNull
    private Long brandId;

    @NotNull
    @Length(max = 16)
    private String brandName;

    @NotNull
    @Length(max = 16)
    private String brandAlias;

    @NotNull
    private Long categoryId;

    @Length(max = 64)
    private String name;

    @NotNull
    @DecimalMax("10000000.00")
    private BigDecimal price;

    @NotNull
    private Integer stock;

    @Length(max = 255)
    private String avatar;

    @NotNull
    private LocalDateTime timeCreate;
}
