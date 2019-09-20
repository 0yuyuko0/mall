package com.yuyuko.mall.cart.param;

import com.yuyuko.mall.cart.entity.CartItemDO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class CartAddParam {
    @NotNull
    private Long productId;

    @NotNull
    private Long shopId;

    @NotNull
    @Length(max = 32)
    private String shopName;

    @NotNull
    @Range(min = -1000, max = 1000)
    private Integer count;
}
