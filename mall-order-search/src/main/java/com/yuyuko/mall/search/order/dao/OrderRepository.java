package com.yuyuko.mall.search.order.dao;

import com.yuyuko.mall.search.order.entity.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrderRepository extends ElasticsearchRepository<Order, Long> {

}
