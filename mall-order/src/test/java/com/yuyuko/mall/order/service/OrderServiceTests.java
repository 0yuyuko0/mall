package com.yuyuko.mall.order.service;

import com.yuyuko.mall.order.dto.OrderItemDTO;
import com.yuyuko.mall.order.exception.NotOrderOwnerException;
import com.yuyuko.mall.order.exception.WrongOrderStatusException;
import com.yuyuko.mall.order.param.OrderCreateParam;
import com.yuyuko.mall.order.param.OrderItemCreateParam;
import com.yuyuko.mall.order.param.ShopOrderCreateParam;
import com.yuyuko.mall.redis.core.RedisExpires;
import com.yuyuko.mall.redis.core.RedisUtils;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTests {
    @Autowired
    OrderService orderService;

    @Autowired
    RedisUtils redisUtils;

    @Test
    public void createOrders() throws StockNotEnoughException {
        ShopOrderCreateParam shopOrderCreateParam1 = new ShopOrderCreateParam();
        shopOrderCreateParam1.setOrderItems(List.of(new OrderItemCreateParam(65L, "华为荣耀10 手机", "shit", 3,
                BigDecimal.valueOf(200000, 2))));
        shopOrderCreateParam1.setShopId(2L);
        shopOrderCreateParam1.setShopName("华为自营旗舰店");
        shopOrderCreateParam1.setActualPayment(BigDecimal.valueOf(200000, 2));
        shopOrderCreateParam1.setDeliveryFee(BigDecimal.ZERO);
        shopOrderCreateParam1.setTotalPrice(BigDecimal.valueOf(200000, 2));
        shopOrderCreateParam1.setDeliveryMethod("京东快递");
        shopOrderCreateParam1.setTimeExpectedDelivery("星期六早上");

        ShopOrderCreateParam shopOrderCreateParam2 = new ShopOrderCreateParam();
        shopOrderCreateParam2.setOrderItems(List.of(
                new OrderItemCreateParam(62L, "小米 红米Redmi Note7Pro 手机", "fuck", 3, BigDecimal.valueOf(139900, 2)),
                new OrderItemCreateParam(64L, "小米9透明尊享版", "suck", 3, BigDecimal.valueOf(200000, 2))));
        shopOrderCreateParam2.setShopId(1L);
        shopOrderCreateParam2.setShopName("小米自营旗舰店");
        shopOrderCreateParam2.setActualPayment(BigDecimal.valueOf(339900, 2));
        shopOrderCreateParam2.setDeliveryFee(BigDecimal.ZERO);
        shopOrderCreateParam2.setTotalPrice(BigDecimal.valueOf(339900, 2));
        shopOrderCreateParam2.setDeliveryMethod("顺丰快递");
        shopOrderCreateParam2.setTimeExpectedDelivery("星期一早上");

        OrderCreateParam createParam = new OrderCreateParam();
        createParam.setShopOrders(List.of(shopOrderCreateParam1, shopOrderCreateParam2));
        createParam.setActualPayment(BigDecimal.valueOf(539900, 2));
        createParam.setTotalPrice(BigDecimal.valueOf(539900, 2));
        createParam.setDeliveryFee(BigDecimal.ZERO);
        createParam.setPaymentMethod("微信支付");
        createParam.setConsigneeName("莫锦波");
        createParam.setConsigneePhoneNumber("17520145408");
        createParam.setDeliveryAddress("景湖家园");

        orderService.createOrder(1L, "yuyuko", createParam);
    }


    @Test
    public void payOrder() throws WrongOrderStatusException, StockNotEnoughException, NotOrderOwnerException {
        orderService.payOrder(1L,33L);
    }
}