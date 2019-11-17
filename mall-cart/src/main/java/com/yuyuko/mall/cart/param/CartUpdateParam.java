package com.yuyuko.mall.cart.param;

import com.yuyuko.mall.cart.entity.CartItemDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Valid
@ApiModel
@Accessors(chain = true)
public class CartUpdateParam {
    @NotNull
    @ApiModelProperty(value = "购物车item id",required = true)
    private Long id;

    @NotNull
    @Range(min = 1, max = 1000)
    @ApiModelProperty(value = "修改数量",required = true)
    private Integer count;
}
