package com.yuyuko.mall.product.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateParam implements Serializable {
    @NotNull
    private Long id;

    @NotNull
    private Long sellerId;

    @NotNull
    private Long shopId;

    @NotNull
    private Long brandId;

    @NotNull
    private Long categoryId;

    @Length(max = 64)
    private String name;

    @NotNull
    @DecimalMax("10000000.00")
    private BigDecimal price;

    @NotNull
    @Positive
    private Integer stock;

    @Length(max = 255)
    private String avatar;
}
