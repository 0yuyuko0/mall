package com.yuyuko.mall.cart.param;

import com.yuyuko.mall.cart.entity.CartItemDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Valid
@ApiModel
public class CartUpdateParam {
    @NotNull
    @ApiModelProperty(value = "商品id",required = true)
    private Long productId;

    @NotNull
    @ApiModelProperty(value = "超市id",required = true)
    private Long shopId;

    @NotNull
    @Length(max = 32)
    @ApiModelProperty(value = "超市名",required = true)
    private String shopName;

    @NotNull
    @Range(min = -1000, max = 1000)
    @ApiModelProperty(value = "修改数量",required = true)
    private Integer count;
}
