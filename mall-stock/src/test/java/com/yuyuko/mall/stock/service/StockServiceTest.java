package com.yuyuko.mall.stock.service;

import com.yuyuko.idempotent.api.IdempotentApi;
import com.yuyuko.idempotent.spring.autoconfigure.IdempotentAutoConfiguration;
import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.idgenerator.SnowflakeIdGenerator;
import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.product.message.ProductCreateMessage;
import com.yuyuko.mall.redis.autoconfigure.RedisUtilsAutoConfiguration;
import com.yuyuko.mall.stock.config.DataSourceConfig;
import com.yuyuko.mall.stock.config.IdGeneratorConfig;
import com.yuyuko.mall.stock.config.RedisExpiresConfig;
import com.yuyuko.mall.stock.config.SeataConfig;
import com.yuyuko.mall.stock.dao.StockDao;
import com.yuyuko.mall.stock.dto.StockDTO;
import com.yuyuko.mall.stock.entity.StockDO;
import com.yuyuko.mall.stock.exception.StockDeductRollbackException;
import com.yuyuko.mall.stock.exception.StockNotEnoughException;
import com.yuyuko.mall.stock.manager.StockManager;
import com.yuyuko.mall.stock.param.StockDeductParam;
import com.yuyuko.mall.stock.service.StockService;
import com.yuyuko.mall.stock.service.tcc.StockDeductTccActionImpl;
import io.seata.rm.tcc.api.LocalTCC;
import org.apache.commons.dbcp2.managed.TransactionContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMybatis
@DataRedisTest(
        includeFilters = {
                @ComponentScan.Filter(LocalTCC.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        StockService.class,
                        StockService.ProductCreateMessageListener.class,
                        StockDeductTccActionImpl.class,
                        StockManager.class
                })
        }
)
@Import({
        DataSourceConfig.class,
        SeataConfig.class,
        IdGeneratorConfig.class,
        RedisExpiresConfig.class,
})
@ImportAutoConfiguration({
        RedisUtilsAutoConfiguration.class,
        IdempotentAutoConfiguration.class,
        AopAutoConfiguration.class
})
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockDao stockDao;

    @SpyBean
    private StockManager stockManager;

    @MockBean
    private MessageCodec messageCodec;

    @BeforeEach
    void insertTestData() {
        StockDO stockDO = new StockDO();
        stockDO.setId(-1L);
        stockDO.setProductId(-1L);
        stockDO.setStock(50);
        stockDao.insert(stockDO);
        stockDO.setId(-2L);
        stockDO.setProductId(-2L);
        stockDO.setStock(100);
        stockDao.insert(stockDO);
    }

    @AfterEach
    void removeTestData() {
        stockDao.deleteByProductId(-1L);
        stockDao.deleteByProductId(-2L);
    }

    @ParameterizedTest
    @MethodSource("deductStockGen")
    void deductStock(List<StockDeductParam> stockDeductParams, Class<? extends Throwable> wEx,
                     List<Integer> wStocks) throws
            StockDeductRollbackException, StockNotEnoughException {
        if (wEx != null) {
            assertThrows(wEx, () -> stockService.deductStock(stockDeductParams));
            return;
        }
        stockService.deductStock(stockDeductParams);
        List<StockDTO> stocks =
                stockDao.listStocks(stockDeductParams.stream().map(StockDeductParam::getProductId).collect(Collectors.toList()));
        assertEquals(wStocks, stocks.stream().map(StockDTO::getStock).collect(Collectors.toList()));
    }

    @Test
    void deductStockRollback() throws StockDeductRollbackException, StockNotEnoughException {
        stockService.deductStock(List.of(new StockDeductParam(-1L, 50), new StockDeductParam(-2L,
                100)));

        assertEquals(List.of(0, 0),
                stockDao.listStocks(List.of(-1L, -2L)).stream().map(StockDTO::getStock).collect(Collectors.toList()));

        //假的stock
        when(stockManager.listStocks(List.of(-1L, -2L))).thenAnswer(invocation -> List.of(50, 100));

        assertThrows(StockDeductRollbackException.class, () ->
                stockService.deductStock(List.of(new StockDeductParam(-1L, 50),
                        new StockDeductParam(-2L,
                                100))));
        reset(stockManager);

        assertEquals(List.of(0, 0),
                stockDao.listStocks(List.of(-1L, -2L)).stream().map(StockDTO::getStock).collect(Collectors.toList()));
    }

    static Stream<Arguments> deductStockGen() {
        return Stream.of(
                of(
                        List.of(new StockDeductParam(-1L, 20), new StockDeductParam(-2L, 50)),
                        null,
                        List.of(30, 50)
                ),
                of(
                        List.of(new StockDeductParam(-1L, 50), new StockDeductParam(-2L, 100)),
                        null,
                        List.of(0, 0)
                ),
                of(
                        List.of(new StockDeductParam(-1L, 51), new StockDeductParam(-2L, 100)),
                        StockNotEnoughException.class,
                        null
                )
        );
    }

    @Autowired
    private RocketMQListener<MessageExt> productCreateMessageListener;

    @Test
    @Transactional
    void onRegisterMessage() {
        MessageExt messageExt = new MessageExt();
        ProductCreateMessage createMessage = new ProductCreateMessage();
        createMessage.setId(-3L);
        createMessage.setStock(100);
        messageExt.setBody(messageCodec.encode(createMessage));
        productCreateMessageListener.onMessage(messageExt);
        assertEquals(100, stockDao.getStock(-3L));
    }
}