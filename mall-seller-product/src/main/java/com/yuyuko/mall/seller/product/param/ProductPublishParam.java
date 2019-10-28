package com.yuyuko.mall.seller.product.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ProductPublishParam {
    @NotNull
    @ApiModelProperty(position = 1)
    private Long brandId;

    @NotNull
    @ApiModelProperty(position = 2)
    private String brandName;

    @ApiModelProperty(position = 3)
    private String brandAlias;

    @NotNull
    @ApiModelProperty(position = 4)
    private Long categoryId;

    @Length(max = 64)
    @ApiModelProperty(position = 5,value = "长度不超过64个字符")
    private String name;

    @DecimalMax("10000000.00")
    @DecimalMin("0.01")
    @ApiModelProperty(position = 6)
    private BigDecimal price;

    @Min(0)
    @Max(Integer.MAX_VALUE)
    @ApiModelProperty(position = 7)
    private Integer stock;

    @NotNull
    @Length(max = 255)
    @ApiModelProperty(position = 8)
    private String avatar;
}