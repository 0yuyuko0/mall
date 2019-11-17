package com.yuyuko.mall.order.service;

import com.yuyuko.mall.order.config.DataSourceConfig;
import com.yuyuko.mall.order.config.IdGeneratorConfig;
import com.yuyuko.mall.order.config.MessageCodecConfig;
import com.yuyuko.mall.order.dao.OrderDao;
import com.yuyuko.mall.order.dao.OrderItemDao;
import com.yuyuko.mall.order.dto.OrderDTO;
import com.yuyuko.mall.order.dto.OrderItemDTO;
import com.yuyuko.mall.order.exception.NotOrderOwnerException;
import com.yuyuko.mall.order.exception.WrongOrderStatusException;
import com.yuyuko.mall.order.manager.OrderManager;
import com.yuyuko.mall.order.message.OrderItemPersistMessage;
import com.yuyuko.mall.order.message.OrderPersistMessage;
import com.yuyuko.mall.order.param.OrderCreateParam;
import com.yuyuko.mall.order.param.OrderItemCreateParam;
import com.yuyuko.mall.order.param.ShopOrderCreateParam;
import com.yuyuko.mall.stock.api.StockRemotingService;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.param.StockDeductParam;
import com.yuyuko.mall.test.autoconfigure.rocketmq.RocketMQTest;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.yuyuko.mall.order.entity.OrderStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RocketMQTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                OrderService.class,
                OrderService.OrderCreateTxMessageListener.class,
                OrderService.OrderCancelTxMessageListener.class,
                OrderService.OrderPayTxMessageListener.class,
        }
))
@AutoConfigureMybatis
@Import({
        IdGeneratorConfig.class,
        MessageCodecConfig.class,
})
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @SpyBean
    private OrderService.OrderCreateTxMessageListener createTxMessageListener;

    @SpyBean
    private OrderService.OrderPayTxMessageListener payTxMessageListener;

    @SpyBean
    private OrderService.OrderCancelTxMessageListener cancelTxMessageListener;

    @MockBean
    private StockRemotingService stockRemotingService;

    @MockBean
    private OrderDao orderDao;

    @MockBean
    private OrderItemDao orderItemDao;

    @MockBean
    private OrderManager orderManager;

    @BeforeEach
    void mockStockRemotingService() {
        ReflectionTestUtils.setField(orderService, "stockRemotingService", stockRemotingService);
    }

    @Test
    void createOrder() throws InterruptedException {
        doAnswer(invocation -> {
            OrderPersistMessage orderPersistMessage = invocation.getArgument(1);
            assertTrue(Arrays.stream(orderPersistMessage.getClass().getDeclaredFields()).allMatch(field -> ReflectionTestUtils.getField(orderPersistMessage,
                    field.getName()) != null));
            List<OrderItemPersistMessage> orderItemPersistMessages =
                    (List<OrderItemPersistMessage>) ReflectionTestUtils.getField(orderPersistMessage,
                            "orderItemPersistMessages");
            assertEquals(1, orderItemPersistMessages.size());
            assertTrue(Arrays.stream(orderItemPersistMessages.get(0).getClass().getDeclaredFields())
                    .allMatch(field -> ReflectionTestUtils.getField(orderItemPersistMessages.get(0),
                            field.getName()) != null));
            return null;
        }).when(createTxMessageListener).persist(any());

        OrderCreateParam createParam = new OrderCreateParam()
                .setActualPayment(BigDecimal.ZERO)
                .setConsigneeName("")
                .setConsigneePhoneNumber("")
                .setDeliveryAddress("")
                .setDeliveryFee(BigDecimal.ZERO)
                .setPaymentMethod("")
                .setTotalPrice(BigDecimal.ZERO)
                .setShopOrders(
                        List.of(new ShopOrderCreateParam()
                                .setShopId(1L)
                                .setShopName("")
                                .setDeliveryFee(BigDecimal.ZERO)
                                .setTotalPrice(BigDecimal.ZERO)
                                .setActualPayment(BigDecimal.ZERO)
                                .setDeliveryMethod("")
                                .setTimeExpectedDelivery("")
                                .setOrderItems(
                                        List.of(
                                                new OrderItemCreateParam()
                                                        .setId(1L)
                                                        .setName("")
                                                        .setPrice(BigDecimal.ZERO)
                                                        .setAvatar("")
                                                        .setCount(1)
                                        ))
                        ));
        orderService.createOrder(1L, "yuyuko", createParam);

        when(orderDao.insert(any())).thenThrow(RuntimeException.class);
        when(orderDao.exist(anyLong())).thenReturn(true);
        AtomicReference<Boolean> done = new AtomicReference<>();
        when(createTxMessageListener.checkLocalTransaction(any())).thenAnswer(invocation -> {
            RocketMQLocalTransactionState state =
                    (RocketMQLocalTransactionState) invocation.callRealMethod();
            done.set(state == RocketMQLocalTransactionState.COMMIT);
            return state;
        });

        while (done.get() == null)
            TimeUnit.MILLISECONDS.sleep(500);

        assertTrue(done.get());
    }

    @Test
    void payOrder() throws StockNotEnoughException, WrongOrderStatusException,
            NotOrderOwnerException, InterruptedException {
        OrderDTO order = new OrderDTO();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus(WAIT_PAY);
        order.setOrderItems(List.of(new OrderItemDTO().setId(1L).setCount(3)));

        when(orderManager.getOrder(1L)).thenReturn(order);

        when(orderDao.updateOrderStatus(anyLong(), anyInt())).thenAnswer(invocation -> {
            assertEquals(1L, ((Long) invocation.getArgument(0)));
            assertEquals(WAIT_SEND, (int) invocation.getArgument(1));
            return null;
        });

        doAnswer(invocation -> {
            List<StockDeductParam> stockDeductParams = invocation.getArgument(0);
            assertEquals(1, stockDeductParams.size());
            assertEquals(1L, stockDeductParams.get(0).getProductId());
            assertEquals(3, stockDeductParams.get(0).getCount());
            return null;
        }).when(stockRemotingService).deductStock(anyList());

        orderService.payOrder(1L, 1L);

        reset(orderDao);
        reset(stockRemotingService);

        doThrow(StockNotEnoughException.class).when(stockRemotingService).deductStock(anyList());

        assertThrows(StockNotEnoughException.class, () -> orderService.payOrder(1L, 1L));

        when(orderDao.updateOrderStatus(anyLong(), anyInt())).thenThrow(RuntimeException.class);
        when(orderDao.checkOrderStatus(anyLong(), anyInt())).thenReturn(true);
        AtomicReference<Boolean> done = new AtomicReference<>();
        when(payTxMessageListener.checkLocalTransaction(any())).thenAnswer(invocation -> {
            RocketMQLocalTransactionState o =
                    (RocketMQLocalTransactionState) invocation.callRealMethod();
            done.set(o == RocketMQLocalTransactionState.COMMIT);
            return o;
        });


        orderService.payOrder(1L, 1L);


        while (done.get() == null)
            TimeUnit.MILLISECONDS.sleep(500);

        assertTrue(done.get());
    }

    @Test
    void cancelOrder() throws WrongOrderStatusException, StockNotEnoughException, NotOrderOwnerException, InterruptedException {
        OrderDTO order = new OrderDTO();
        order.setId(1L);
        order.setUserId(1L);
        order.setStatus(WAIT_PAY);
        order.setOrderItems(List.of(new OrderItemDTO().setId(1L).setCount(3)));

        when(orderManager.getOrder(1L)).thenReturn(order);

        when(orderDao.updateOrderStatus(anyLong(), anyInt())).thenAnswer(invocation -> {
            assertEquals(1L, ((Long) invocation.getArgument(0)));
            assertEquals(CANCELED, (int) invocation.getArgument(1));
            return null;
        });

        orderService.cancelOrder(1L, 1L);

        reset(orderDao);

        when(orderDao.updateOrderStatus(anyLong(), anyInt())).thenThrow(RuntimeException.class);
        when(orderDao.checkOrderStatus(anyLong(), anyInt())).thenReturn(true);
        AtomicReference<Boolean> done = new AtomicReference<>();
        when(cancelTxMessageListener.checkLocalTransaction(any())).thenAnswer(invocation -> {
            RocketMQLocalTransactionState o =
                    (RocketMQLocalTransactionState) invocation.callRealMethod();
            done.set(o == RocketMQLocalTransactionState.COMMIT);
            return o;
        });


        orderService.cancelOrder(1L, 1L);


        while (done.get() == null)
            TimeUnit.MILLISECONDS.sleep(500);

        assertTrue(done.get());
    }

    @Test
    void getOrder() {
    }
}