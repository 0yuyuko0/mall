package com.yuyuko.mall.order.dao;

import com.yuyuko.mall.order.dto.OrderItemDTO;
import com.yuyuko.mall.order.entity.OrderItemDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface OrderItemDao {
    int createOrderItems(List<OrderItemDO>orderItemDOs);

    List<OrderItemDTO> listOrderItems(Long orderId);
}