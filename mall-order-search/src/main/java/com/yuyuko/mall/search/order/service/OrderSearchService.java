package com.yuyuko.mall.search.order.service;

import com.yuyuko.idempotent.annotation.Idempotent;
import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.order.message.OrderCancelMessage;
import com.yuyuko.mall.order.message.OrderCreateMessage;
import com.yuyuko.mall.order.message.OrderPayMessage;
import com.yuyuko.mall.order.message.OrderStatus;
import com.yuyuko.mall.search.order.dao.OrderRepository;
import com.yuyuko.mall.search.order.entity.Order;
import com.yuyuko.mall.search.order.param.OrderSearchParam;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateAction;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;

@Service
public class OrderSearchService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @RocketMQMessageListener(
            consumerGroup = "order-search",
            topic = "order",
            selectorExpression = "create")
    @Service
    public class OrderCreateMessageListener implements RocketMQListener<MessageExt> {
        @Autowired
        private MessageCodec messageCodec;

        @Override
        @Idempotent(id = "#message.getKeys()")
        public void onMessage(MessageExt message) {
            OrderCreateMessage orderCreateMessage = messageCodec.decode(message.getBody(),
                    OrderCreateMessage.class);
            createOrder(orderCreateMessage);
        }

        public void createOrder(OrderCreateMessage orderCreateMessage) {
            Order order = new Order();
            BeanUtils.copyProperties(orderCreateMessage, order);
            if (!orderRepository.existsById(order.getId()))
                orderRepository.save(order);
        }
    }

    @RocketMQMessageListener(
            consumerGroup = "order-search",
            topic = "order",
            selectorExpression = "pay||cancel")
    @Service
    public class OrderStatusChangeMessageListener implements RocketMQListener<MessageExt> {
        @Autowired
        private MessageCodec messageCodec;

        @Override
        @Idempotent(id = "#message.getKeys()")
        public void onMessage(MessageExt message) {
            long orderId;
            int status;
            switch (message.getTags()) {
                case "pay":
                    OrderPayMessage payMessage = messageCodec.decode(message.getBody(),
                            OrderPayMessage.class);
                    orderId = payMessage.getId();
                    status = OrderStatus.WAIT_SEND;
                    break;
                case "cancel":
                    OrderCancelMessage cancelMessage = messageCodec.decode(message.getBody(),
                            OrderCancelMessage.class);
                    orderId = (cancelMessage.getId());
                    status = OrderStatus.CANCELED;
                    break;
                default:
                    return;
            }
            updateOrderStatus(orderId, status);
        }
    }

    @Value("${elasticsearch.index}")
    public String index;

    @Value("${elasticsearch.type}")
    public String type;


    private void updateOrderStatus(long orderId, int status) {
        elasticsearchTemplate.update(
                new UpdateQueryBuilder()
                        .withUpdateRequest(
                                new UpdateRequest().doc(Map.of("status", status)))
                        .withId(Long.toString(orderId))
                        .withClass(Order.class)
                        .build()
        );
    }

    private int orderPageSize = 10;

    public Page<Order> searchOrders(Long userId, OrderSearchParam orderSearchParam) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        BoolQueryBuilder queryBuilder =
                boolQuery().filter(termQuery("userId", userId));
        if (orderSearchParam.getKeyword() != null) {
            queryBuilder.must(
                    multiMatchQuery(orderSearchParam.getKeyword())
                            .field("orderItems.name", 3.0f)
                            .field("shopName", 1.0f)
                            .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
            );
        }
        if (orderSearchParam.getOrderId() != null)
            queryBuilder.filter(termQuery("id", orderSearchParam.getOrderId()));
        if (orderSearchParam.getProductId() != null)
            queryBuilder.filter(termQuery("orderItems.id", orderSearchParam.getProductId()));
        if (orderSearchParam.getStatus() != null)
            queryBuilder.filter(termQuery("status", orderSearchParam.getStatus()));
        if (orderSearchParam.getTimeRange() != null) {
            OrderSearchParam.TimeRange timeRange = orderSearchParam.getTimeRange();
            RangeQueryBuilder timeCreateRangeQuery = rangeQuery("timeCreate")
                    .format("yyyy-MM-dd'T'HH:mm:ss");
            if (timeRange.getFrom() != null)
                timeCreateRangeQuery.from(timeRange.getFrom());
            if (timeRange.getTo() != null)
                timeCreateRangeQuery.to(timeRange.getTo());
            queryBuilder.filter(timeCreateRangeQuery);
        }

        builder
                .withQuery(queryBuilder)
                .withSort(fieldSort("timeCreate").order(SortOrder.DESC))
                .withSearchType(SearchType.DEFAULT)
                .withPageable(PageRequest.of(orderSearchParam.getPage(), orderPageSize));

        return orderRepository.search(builder.build());
    }
}
