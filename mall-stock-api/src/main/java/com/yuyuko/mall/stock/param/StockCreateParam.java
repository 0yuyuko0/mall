package com.yuyuko.mall.stock.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockCreateParam implements Serializable {
    @NotNull
    private Long productId;

    @Positive
    private Integer stock;
}
