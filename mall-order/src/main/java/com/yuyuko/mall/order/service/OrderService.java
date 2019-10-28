package com.yuyuko.mall.order.service;

import com.yuyuko.idempotent.annotation.Idempotent;
import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.order.dto.OrderDTO;
import com.yuyuko.mall.order.dto.OrderItemDTO;
import com.yuyuko.mall.order.exception.NotOrderOwnerException;
import com.yuyuko.mall.order.exception.WrongOrderStatusException;
import com.yuyuko.mall.common.utils.CollectionUtils;
import com.yuyuko.mall.order.dao.OrderDao;
import com.yuyuko.mall.order.dao.OrderItemDao;
import com.yuyuko.mall.order.entity.OrderDO;
import com.yuyuko.mall.order.entity.OrderItemDO;
import com.yuyuko.mall.order.manager.OrderManager;
import com.yuyuko.mall.order.message.*;
import com.yuyuko.mall.order.param.OrderCreateParam;
import com.yuyuko.mall.stock.api.StockRemotingService;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.param.StockDeductParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yuyuko.mall.order.entity.OrderStatus.*;

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
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private MessageCodec messageCodec;

    @Reference
    private StockRemotingService stockRemotingService;

    public void createOrder(Long userId, String nickname, OrderCreateParam createParam) {
        //持久化消息
        List<OrderPersistMessage> orderPersistMessages = buildOrderPersistMessages(userId,
                createParam);

        List<OrderCreateMessage> orderCreateMessages = buildOrderCreateMessages(userId, nickname,
                orderPersistMessages);

        CollectionUtils.consume(orderCreateMessages, orderPersistMessages, (orderCreateMessage,
                                                                            orderPersistMessage) -> {
            rocketMQTemplate.sendMessageInTransaction(
                    "tx-order-create",
                    "order:create",
                    MessageBuilder.withPayload(messageCodec.encode(orderCreateMessage))
                            .setHeader(RocketMQHeaders.KEYS, idGenerator.nextId())
                            .build(),
                    orderPersistMessage
            );
        });
    }

    @RocketMQTransactionListener(txProducerGroup = "tx-order-create")
    public class OrderCreateTxMessageListener implements RocketMQLocalTransactionListener {
        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            OrderPersistMessage orderPersistMessage = (OrderPersistMessage) arg;
            ((OrderCreateTxMessageListener) AopContext.currentProxy()).persist(orderPersistMessage);
            return RocketMQLocalTransactionState.COMMIT;
        }

        @Transactional
        public void persist(OrderPersistMessage orderPersistMessage) {
            OrderDO orderDO = buildOrderDO(orderPersistMessage);
            List<OrderItemDO> orderItemDOs = buildOrderItemDos(orderPersistMessage);
            try {
                orderDao.insert(orderDO);
                orderItemDao.createOrderItems(orderItemDOs);
            } catch (DataIntegrityViolationException ignore) {//幂等

            } catch (Throwable ex) {
                log.error(ex.getMessage());
                throw ex;
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

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            OrderCreateMessage createMessage =
                    messageCodec.decode((byte[]) msg.getPayload(),
                            OrderCreateMessage.class);
            if (orderDao.exist(createMessage.getId()).orElse(false))
                return RocketMQLocalTransactionState.COMMIT;
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    private List<OrderPersistMessage> buildOrderPersistMessages(Long userId,
                                                                OrderCreateParam createParam) {
        return CollectionUtils.convertTo(createParam.getShopOrders(), shopOrderCreateParam -> {
            OrderPersistMessage orderPersistMessage = new OrderPersistMessage();
            orderPersistMessage.setId(idGenerator.nextId());
            orderPersistMessage.setUserId(userId);
            orderPersistMessage.setShopId(shopOrderCreateParam.getShopId());
            orderPersistMessage.setShopName(shopOrderCreateParam.getShopName());
            orderPersistMessage.setStatus(WAIT_PAY);
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
                        orderItemPersistMessage.setId(idGenerator.nextId());
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


    public void payOrder(Long userId, Long orderId) throws WrongOrderStatusException,
            NotOrderOwnerException, StockNotEnoughException {
        OrderDTO order = orderManager.getOrder(orderId);
        checkOrderOwner(order.getId(), userId, order.getUserId());
        checkOrderStatus(order.getId(), WAIT_PAY, order.getStatus());
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
                "tx-order-pay", "order:pay",
                MessageBuilder.withPayload(messageCodec.encode(buildOrderPayMessage(userId,
                        orderId)))
                        .setHeader(RocketMQHeaders.KEYS, idGenerator.nextId())
                        .build(),
                order.getOrderItems());
        if (sendResult.getLocalTransactionState() == LocalTransactionState.ROLLBACK_MESSAGE)
            throw new StockNotEnoughException();
    }

    @RocketMQTransactionListener(txProducerGroup = "tx-order-pay")
    public class OrderPayTxMessageListener implements RocketMQLocalTransactionListener {
        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            List<OrderItemDTO> orderItemDTOS = (List<OrderItemDTO>) arg;
            try {
                deductStock(orderItemDTOS);
            } catch (StockNotEnoughException e) {
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            return RocketMQLocalTransactionState.COMMIT;
        }

        private void deductStock(List<OrderItemDTO> orderItems) throws StockNotEnoughException {
            List<StockDeductParam> stockDeductParam =
                    buildStockDeductParam(orderItems);
            stockRemotingService.deductStock(stockDeductParam);
        }

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            OrderPayMessage payMessage = messageCodec.decode(((byte[]) msg.getPayload()),
                    OrderPayMessage.class);
            if (orderDao.checkOrderStatus(payMessage.getId(), WAIT_SEND).orElse(false))
                return RocketMQLocalTransactionState.COMMIT;
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @RocketMQMessageListener(
            consumerGroup = "order-order",
            topic = "order",
            selectorExpression = "pay"
    )
    @Service
    public class OrderStatusChangeMessageListener implements RocketMQListener<MessageExt> {
        @Override
        @Idempotent(id = "#message.getKeys()")
        public void onMessage(MessageExt message) {
            long id;
            int status;
            switch (message.getTags()) {
                case "pay":
                    OrderPayMessage payMessage = messageCodec.decode(message.getBody(),
                            OrderPayMessage.class);
                    id = payMessage.getId();
                    status = WAIT_SEND;
                    break;
                default:
                    log.warn("未知的tag[{}]", message.getTags());
                    return;
            }
            orderDao.updateOrderStatus(id, status);
        }
    }

    private OrderPayMessage buildOrderPayMessage(Long userId, Long orderId) {
        return new OrderPayMessage(orderId, userId);
    }

    private List<StockDeductParam> buildStockDeductParam(List<OrderItemDTO> orderItems) {
        return orderItems.stream().map(orderItemDTO -> {
            StockDeductParam stockDeductParam = new StockDeductParam();
            stockDeductParam.setProductId(orderItemDTO.getId());
            stockDeductParam.setCount(orderItemDTO.getCount());
            return stockDeductParam;
        }).collect(Collectors.toList());
    }

    public void cancelOrder(Long userId, Long orderId) throws NotOrderOwnerException,
            WrongOrderStatusException {
        OrderDTO order = orderManager.getOrder(orderId);
        checkOrderOwner(orderId, userId, order.getUserId());
        checkOrderStatus(orderId, WAIT_PAY, order.getStatus());
        rocketMQTemplate.sendMessageInTransaction("tx-order-cancel", "order:cancel",
                MessageBuilder.withPayload(messageCodec.encode(buildOrderCancelMessage(userId,
                        orderId)))
                        .setHeader(RocketMQHeaders.KEYS, idGenerator.nextId())
                        .build(), order.getId());
    }

    @RocketMQTransactionListener(txProducerGroup = "tx-order-cancel")
    public class OrderCancelTxMessageListener implements RocketMQLocalTransactionListener {
        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            Long orderId = (Long) arg;
            orderDao.updateOrderStatus(orderId, CANCELED);
            return RocketMQLocalTransactionState.COMMIT;
        }

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            OrderCancelMessage message = messageCodec.decode(((byte[]) msg.getPayload()),
                    OrderCancelMessage.class);
            if (orderDao.checkOrderStatus(message.getId(), CANCELED).orElse(false))
                return RocketMQLocalTransactionState.COMMIT;
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }


    private OrderCancelMessage buildOrderCancelMessage(Long userId, Long orderId) {
        return new OrderCancelMessage(orderId, userId);
    }

    public OrderDTO getOrder(Long orderId, Long userId) throws NotOrderOwnerException {
        OrderDTO order = orderManager.getOrder(orderId);
        checkOrderOwner(orderId, userId, order.getUserId());
        return order;
    }

    private void checkOrderOwner(long orderId, long expected, long userId) throws NotOrderOwnerException {
        if (userId != expected) {
            log.warn("用户id[{}] 不是订单[{}]的拥有者，该订单的拥有者为[{}]", userId, orderId, expected);
            throw new NotOrderOwnerException();
        }
    }

    private void checkOrderStatus(long orderId, int expected, int status) throws WrongOrderStatusException {
        if (status != expected) {
            log.warn("订单号[{}]状态不正确 期望状态[{}]，实际状态[{}]", orderId, expected, status);
            throw new WrongOrderStatusException();
        }
    }

}