package com.yuyuko.mall.order.service;

import com.yuyuko.mall.common.utils.ProtoStuffUtils;
import com.yuyuko.mall.common.utils.SnowflakeIdGenerator;
import com.yuyuko.mall.order.dto.OrderDTO;
import com.yuyuko.mall.order.dto.OrderItemDTO;
import com.yuyuko.mall.order.exception.NotOrderOwnerException;
import com.yuyuko.mall.order.exception.WrongOrderStatusException;
import com.yuyuko.mall.order.dto.OrderStatus;
import com.yuyuko.mall.common.utils.CollectionUtils;
import com.yuyuko.mall.order.dao.OrderDao;
import com.yuyuko.mall.order.dao.OrderItemDao;
import com.yuyuko.mall.order.entity.OrderDO;
import com.yuyuko.mall.order.entity.OrderItemDO;
import com.yuyuko.mall.order.manager.OrderManager;
import com.yuyuko.mall.order.message.OrderCreateMessage;
import com.yuyuko.mall.order.message.OrderItemCreateMessage;
import com.yuyuko.mall.order.message.OrderItemPersistMessage;
import com.yuyuko.mall.order.message.OrderPersistMessage;
import com.yuyuko.mall.order.param.OrderCreateParam;
import com.yuyuko.mall.stock.api.StockService;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.param.StockDeductParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-13
 */
@Service
@Slf4j
public class OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    OrderManager orderManager;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Reference
    private StockService stockService;

    public void payOrder(Long userId, Long orderId) throws WrongOrderStatusException,
            NotOrderOwnerException, StockNotEnoughException {
        OrderDTO order = orderManager.getOrder(orderId);
        checkOrderOwner(order.getUserId(), userId);
        checkOrderStatus(order.getStatus(), OrderStatus.WAIT_PAY);
        deductStock(order.getOrderItems());
    }

    private void checkOrderOwner(long userId, long expected) throws NotOrderOwnerException {
        if (userId != expected)
            throw new NotOrderOwnerException();
    }

    private void checkOrderStatus(int status, int expected) throws WrongOrderStatusException {
        if (status != expected)
            throw new WrongOrderStatusException();
    }

    private List<StockDeductParam> buildStockDeductParam(List<OrderItemDTO> orderItems) {
        return orderItems.stream().map(orderItemDTO -> {
            StockDeductParam stockDeductParam = new StockDeductParam();
            stockDeductParam.setProductId(orderItemDTO.getId());
            stockDeductParam.setCount(orderItemDTO.getCount());
            return stockDeductParam;
        }).collect(Collectors.toList());
    }

    private void deductStock(List<OrderItemDTO> orderItems) throws StockNotEnoughException {
        List<StockDeductParam> stockDeductParam =
                buildStockDeductParam(orderItems);
        stockService.deductStock(stockDeductParam);
    }

    public void createOrder(Long userId, String nickname, OrderCreateParam createParam) {

        //持久化消息
        List<OrderPersistMessage> orderPersistMessages = buildOrderPersistMessages(userId,
                createParam);

        List<OrderCreateMessage> orderCreateMessages = buildOrderCreateMessages(userId, nickname,
                orderPersistMessages);

        CollectionUtils.consume(orderCreateMessages, orderPersistMessages, (orderCreateMessage, orderPersistMessage) -> {
            rocketMQTemplate.sendMessageInTransaction(
                    "tx-order",
                    "order:create",
                    MessageBuilder.withPayload(ProtoStuffUtils.serialize(orderCreateMessage)).build(),
                    orderPersistMessage
            );
        });
    }

    @RocketMQTransactionListener(txProducerGroup = "tx-order")
    public class OrderCreateTxMessageListener implements RocketMQLocalTransactionListener {
        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            OrderPersistMessage orderPersistMessage = (OrderPersistMessage) arg;
            rocketMQTemplate.syncSend("order:persist",
                    MessageBuilder.withPayload(ProtoStuffUtils.serialize(orderPersistMessage)).build());
            return RocketMQLocalTransactionState.COMMIT;
        }

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            OrderCreateMessage createMessage =
                    ProtoStuffUtils.deserialize((byte[]) msg.getPayload(),
                            OrderCreateMessage.class);
            Boolean exist = orderDao.exist(createMessage.getId());
            if (exist != null && exist)
                return RocketMQLocalTransactionState.COMMIT;
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @RocketMQMessageListener(
            consumerGroup = "order-order",
            topic = "order",
            selectorExpression = "persist"
    )
    @Service
    public class OrderPersistMessageListener implements RocketMQListener<MessageExt> {
        @Override
        public void onMessage(MessageExt message) {
            OrderPersistMessage orderPersistMessage = ProtoStuffUtils.deserialize(message.getBody(),
                    OrderPersistMessage.class);
            persist(orderPersistMessage);
        }

        @Transactional
        public void persist(OrderPersistMessage orderPersistMessage) {
            OrderDO orderDO = buildOrderDO(orderPersistMessage);
            List<OrderItemDO> orderItemDOs = buildOrderItemDos(orderPersistMessage);
            try {
                orderDao.insert(orderDO);
                orderItemDao.createOrderItems(orderItemDOs);
            } catch (Throwable ex) {
                log.error(ex.getMessage());
            }
        }

        private List<OrderItemDO> buildOrderItemDos(OrderPersistMessage orderPersistMessage) {
            List<OrderItemDO> orderItemDOs = new ArrayList<>();
            orderPersistMessage.getOrderItemPersistMessages().forEach(orderItemPersistMessage -> {
                OrderItemDO orderItemDO = new OrderItemDO();
                BeanUtils.copyProperties(orderItemPersistMessage, orderItemDO);
                orderItemDOs.add(orderItemDO);
            });
            return orderItemDOs;
        }

        private OrderDO buildOrderDO(OrderPersistMessage orderPersistMessage) {
            OrderDO orderDO = new OrderDO();
            BeanUtils.copyProperties(orderPersistMessage, orderDO);
            return orderDO;
        }
    }

    private List<OrderPersistMessage> buildOrderPersistMessages(Long userId,
                                                                OrderCreateParam createParam) {
        return CollectionUtils.convertTo(createParam.getShopOrders(), shopOrderCreateParam -> {
            OrderPersistMessage orderPersistMessage = new OrderPersistMessage();
            orderPersistMessage.setId(snowflakeIdGenerator.nextId());
            orderPersistMessage.setUserId(userId);
            orderPersistMessage.setShopId(shopOrderCreateParam.getShopId());
            orderPersistMessage.setShopName(shopOrderCreateParam.getShopName());
            orderPersistMessage.setStatus(OrderStatus.WAIT_PAY);
            orderPersistMessage.setPaymentMethod(createParam.getPaymentMethod());
            orderPersistMessage.setTotalPrice(shopOrderCreateParam.getTotalPrice());
            orderPersistMessage.setDeliveryFee(shopOrderCreateParam.getDeliveryFee());
            orderPersistMessage.setActualPayment(shopOrderCreateParam.getActualPayment());
            orderPersistMessage.setDeliveryMethod(shopOrderCreateParam.getDeliveryMethod());
            orderPersistMessage.setTimeExpectedDelivery(shopOrderCreateParam.getTimeExpectedDelivery());
            orderPersistMessage.setConsigneeName(createParam.getConsigneeName());
            orderPersistMessage.setConsigneePhoneNumber(createParam.getConsigneePhoneNumber());
            orderPersistMessage.setDeliveryAddress(createParam.getDeliveryAddress());
            orderPersistMessage.setTimeCreate(LocalDateTime.now());
            orderPersistMessage.setOrderItemPersistMessages(CollectionUtils.convertTo(shopOrderCreateParam.getOrderItems(),
                    orderItem -> {
                        OrderItemPersistMessage orderItemPersistMessage =
                                new OrderItemPersistMessage();
                        orderItemPersistMessage.setId(snowflakeIdGenerator.nextId());
                        orderItemPersistMessage.setOrderId(orderItemPersistMessage.getId());
                        orderItemPersistMessage.setProductId(orderItem.getId());
                        orderItemPersistMessage.setProductName(orderItem.getName());
                        orderItemPersistMessage.setProductAvatar(orderItem.getAvatar());
                        orderItemPersistMessage.setCount(orderItem.getCount());
                        orderItemPersistMessage.setPrice(orderItem.getPrice());
                        return orderItemPersistMessage;
                    }
            ));
            return orderPersistMessage;
        });
    }

    private List<OrderCreateMessage> buildOrderCreateMessages(
            Long userId, String nickname,
            List<OrderPersistMessage> orderPersistMessages) {
        return CollectionUtils.convertTo(orderPersistMessages, orderPersistMessage -> {
            OrderCreateMessage orderCreateMessage = new OrderCreateMessage();
            orderCreateMessage.setId(orderPersistMessage.getId());
            orderCreateMessage.setUserId(userId);
            orderCreateMessage.setUsername(nickname);
            orderCreateMessage.setShopId(orderPersistMessage.getShopId());
            orderCreateMessage.setShopName(orderPersistMessage.getShopName());
            orderCreateMessage.setStatus(orderPersistMessage.getStatus());
            orderCreateMessage.setTimeCreate(orderPersistMessage.getTimeCreate());
            orderCreateMessage.setOrderItems(CollectionUtils.convertTo(orderPersistMessage.getOrderItemPersistMessages(),
                    orderItemPersistMessage -> {
                        OrderItemCreateMessage orderItemCreateMessage =
                                new OrderItemCreateMessage();
                        orderItemCreateMessage.setId(orderItemPersistMessage.getProductId());
                        orderItemCreateMessage.setName(orderItemPersistMessage.getProductName());
                        orderItemCreateMessage.setAvatar(orderItemPersistMessage.getProductAvatar());
                        orderItemCreateMessage.setCount(orderItemPersistMessage.getCount());
                        orderItemCreateMessage.setPrice(orderItemPersistMessage.getPrice());
                        return orderItemCreateMessage;
                    }));
            return orderCreateMessage;
        });
    }

    public OrderDTO getOrder(Long orderId) {
        return orderManager.getOrder(orderId);
    }
}