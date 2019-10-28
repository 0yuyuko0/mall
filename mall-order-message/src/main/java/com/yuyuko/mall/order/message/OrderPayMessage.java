package com.yuyuko.mall.order.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPayMessage {
    @NotNull
    private Long id;

    @NotNull
    private Long userId;
}
