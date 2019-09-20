package com.yuyuko.mall.stock.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDeductParam {
    @NotNull
    private Long productId;

    @Positive
    private Integer count;
}
