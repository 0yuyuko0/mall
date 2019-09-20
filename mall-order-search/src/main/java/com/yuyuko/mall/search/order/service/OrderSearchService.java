package com.yuyuko.mall.search.order.service;

import com.yuyuko.mall.common.utils.ProtoStuffUtils;
import com.yuyuko.mall.order.message.OrderCreateMessage;
import com.yuyuko.mall.search.order.dao.OrderRepository;
import com.yuyuko.mall.search.order.entity.Order;
import com.yuyuko.mall.search.order.param.OrderSearchParam;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.aspectj.weaver.ast.Or;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;

@Service
public class OrderSearchService {

    @Autowired
    OrderRepository orderRepository;

    @RocketMQMessageListener(
            consumerGroup = "search-order",
            topic = "order",
            selectorExpression = "create")
    @Service
    public class OrderCreateMessageListener implements RocketMQListener<MessageExt> {
        @Override
        public void onMessage(MessageExt message) {
            OrderCreateMessage orderCreateMessage = ProtoStuffUtils.deserialize(message.getBody(),
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
        if (orderSearchParam.getTimeCreate() != null) {
            OrderSearchParam.TimeCreateRange timeCreateRange = orderSearchParam.getTimeCreate();
            RangeQueryBuilder timeCreateRangeQuery = rangeQuery("timeCreate")
                    .format("yyyy-MM-dd'T'HH:mm:ss");
            if (timeCreateRange.getFrom() != null)
                timeCreateRangeQuery.from(timeCreateRange.getFrom().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            if (timeCreateRange.getTo() != null)
                timeCreateRangeQuery.to(timeCreateRange.getTo().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
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
