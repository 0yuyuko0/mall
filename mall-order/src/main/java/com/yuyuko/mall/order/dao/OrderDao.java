package com.yuyuko.mall.order.dao;

import com.yuyuko.mall.order.dto.OrderDTO;
import com.yuyuko.mall.order.entity.OrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-13
 */
@Mapper
public interface OrderDao {
    int insert(OrderDO orderDO);

    OrderDTO getOrder(Long id);

    int createOrders(List<OrderDO> orderDOs);

    Optional<Boolean> exist(Long id);

    Optional<Boolean> checkOrderStatus(@Param("id") long id, @Param("status") int status);

    int updateOrderStatus(@Param("id") long id, @Param("status") Integer status);
}
