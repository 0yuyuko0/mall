package com.yuyuko.mall.cart.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Valid
@ApiModel
@Accessors(chain = true)
public class CartAddParam {
    @NotNull
    @ApiModelProperty(value = "商品id",required = true)
    private Long productId;

    @ApiModelProperty(value = "超市id：在商品页面添加时不为null，在购物车内添加时可为null（冗余存储）",required = true)
    private Long shopId;

    @Length(max = 32)
    @ApiModelProperty(value = "超市名：在商品页面添加时不为null，在购物车内添加时可为null（冗余存储）",required = true)
    private String shopName;

    @NotNull
    @Range(min = 1, max = 1000)
    @ApiModelProperty(value = "添加数量",required = true)
    private Integer count;
}
