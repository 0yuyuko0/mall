package com.yuyuko.mall.order.dao;

import com.yuyuko.mall.order.dto.OrderDTO;
import com.yuyuko.mall.order.entity.OrderDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-13
 */
@Repository
public interface OrderDao {
    int insert(OrderDO orderDO);
    
    OrderDTO getOrder(Long id);

    int createOrders(List<OrderDO> orderDOs);

    Boolean exist(Long id);
}
