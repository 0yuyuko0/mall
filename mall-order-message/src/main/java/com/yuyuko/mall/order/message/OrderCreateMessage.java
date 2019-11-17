package com.yuyuko.mall.order.message;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class OrderCreateMessage {
    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @Length(max = 16)
    private String username;

    @NotNull
    private Long shopId;

    @Length(max = 32)
    private String shopName;

    @Size(min = 1)
    private List<OrderItemCreateMessage> orderItems;

    @Past
    private LocalDateTime timeCreate;

    @Range(min = 0, max = 8)
    private Integer status;
}
