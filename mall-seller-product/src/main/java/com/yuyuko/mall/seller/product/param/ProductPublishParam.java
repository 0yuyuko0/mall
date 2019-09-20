package com.yuyuko.mall.seller.product.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPublishParam {
    @NotNull
    private Long brandId;

    @NotNull
    private String brandName;

    private String brandAlias;

    @NotNull
    private Long categoryId;

    @Length(max = 64)
    private String name;

    @DecimalMax("10000000.00")
    @DecimalMin("0.01")
    private BigDecimal price;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Integer stock;

    @NotNull
    @Length(max = 255)
    private String avatar;
}