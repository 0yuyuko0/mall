package com.yuyuko.mall.search.order.service;

import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.order.message.*;
import com.yuyuko.mall.search.order.config.ElasticsearchConfig;
import com.yuyuko.mall.search.order.config.MessageCodecConfig;
import com.yuyuko.mall.search.order.dao.OrderRepository;
import com.yuyuko.mall.search.order.param.OrderSearchParam;
import com.yuyuko.mall.test.autoconfigure.elasticsearch.ElasticsearchTest;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

import static com.yuyuko.mall.order.message.OrderStatus.CANCELED;
import static com.yuyuko.mall.order.message.OrderStatus.WAIT_SEND;
import static org.junit.jupiter.api.Assertions.*;

@ElasticsearchTest(
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                OrderSearchService.class,
                OrderSearchService.OrderCreateMessageListener.class,
                OrderSearchService.OrderStatusChangeMessageListener.class,
        })
)
@Import({
        ElasticsearchConfig.class,
        MessageCodecConfig.class
})
class OrderSearchServiceTest {
    @Autowired
    private OrderSearchService orderSearchService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderSearchService.OrderCreateMessageListener orderCreateMessageListener;

    @Autowired
    private OrderSearchService.OrderStatusChangeMessageListener orderStatusChangeMessageListener;

    @Autowired
    private MessageCodec messageCodec;

    @BeforeEach
    void create() {
        OrderCreateMessage createMessage = new OrderCreateMessage()
                .setId(-1L)
                .setShopId(-1L)
                .setShopName("东方旗舰店")
                .setUserId(-1L)
                .setUsername("yuyuko")
                .setStatus(OrderStatus.WAIT_PAY)
                .setTimeCreate(LocalDateTime.now())
                .setOrderItems(List.of(
                        new OrderItemCreateMessage()
                                .setId(-1L)
                                .setName("红魔馆的小萝莉一只")
                                .setPrice(BigDecimal.valueOf(10000))
                                .setAvatar("adw")
                                .setCount(1)
                ));
        MessageExt messageExt = new MessageExt();
        messageExt.setBody(messageCodec.encode(createMessage));

        orderCreateMessageListener.onMessage(messageExt);

        assertTrue(orderRepository.existsById(-1L));
    }

    @AfterEach
    void delete() {
        orderRepository.deleteById(-1L);
    }

    @Test
    void orderStatusChange() {
        MessageExt messageExt = new MessageExt();
        messageExt.setTags("pay");
        messageExt.setBody(messageCodec.encode(
                new OrderPayMessage()
                        .setId(-1L)
                        .setUserId(-1L)
        ));
        orderStatusChangeMessageListener.onMessage(messageExt);
        assertSame(WAIT_SEND, orderRepository.findById(-1L).get().getStatus());

        messageExt.setTags("cancel");
        messageExt.setBody(messageCodec.encode(
                new OrderCancelMessage()
                        .setId(-1L)
                        .setUserId(-1L)
        ));
        orderStatusChangeMessageListener.onMessage(messageExt);
        assertSame(CANCELED, orderRepository.findById(-1L).get().getStatus());
    }

    @Test
    void search() {
        assertTrue(orderSearchService.searchOrders(-1L, new OrderSearchParam()
                .setKeyword("红魔馆")
        ).getContent().size() > 0);
        assertTrue(orderSearchService.searchOrders(-1L, new OrderSearchParam()
                .setKeyword("红魔馆")
                .setOrderId(-1L)
                .setProductId(-1L)
                .setPage(0)
                .setStatus(OrderStatus.WAIT_PAY)
                .setTimeRange(new OrderSearchParam.TimeRange(
                        LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                )
        ).getContent().size() > 0);
    }
}