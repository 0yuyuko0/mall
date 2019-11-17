package com.yuyuko.mall.order.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderPayMessage {
    @NotNull
    private Long id;

    @NotNull
    private Long userId;
}
