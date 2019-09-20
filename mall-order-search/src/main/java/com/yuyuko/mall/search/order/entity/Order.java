package com.yuyuko.mall.search.order.entity;

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
@Document(indexName = "#{elasticsearchConfig.index}",type = "#{elasticsearchConfig.type}")
public class Order {
    @Id
    private Long id;

    private Long userId;

    private String username;

    private Long shopId;

    private String shopName;

    private List<OrderItem> orderItems;

    private LocalDateTime timeCreate;

    private Integer status;
}
