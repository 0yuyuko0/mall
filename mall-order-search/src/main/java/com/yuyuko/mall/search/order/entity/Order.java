package com.yuyuko.mall.search.order.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "#{elasticsearchConfig.index}", type = "#{elasticsearchConfig.type}")
@ApiModel
public class Order {
    @Id
    @ApiModelProperty(position = 1)
    private Long id;

    @ApiModelProperty(position = 2)
    private Long userId;

    @ApiModelProperty(position = 3)
    private String username;

    @ApiModelProperty(position = 4)
    private Long shopId;

    @ApiModelProperty(position = 5)
    private String shopName;

    @ApiModelProperty(position = 6)
    private List<OrderItem> orderItems;

    @ApiModelProperty(position = 7)
    private LocalDateTime timeCreate;

    @ApiModelProperty(position = 8, value = "订单状态：待支付(0),待发货(1),待收货(2),已完成(3),已取消(4)")
    private Integer status;
}