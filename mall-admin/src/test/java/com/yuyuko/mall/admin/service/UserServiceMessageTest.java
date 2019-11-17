package com.yuyuko.mall.admin.service;

import com.yuyuko.mall.admin.config.IdGeneratorConfig;
import com.yuyuko.mall.admin.config.MessageCodecConfig;
import com.yuyuko.mall.admin.dao.UserDao;
import com.yuyuko.mall.admin.entity.UserDO;
import com.yuyuko.mall.admin.exception.UsernameAlreadyExistException;
import com.yuyuko.mall.admin.param.LoginParam;
import com.yuyuko.mall.test.autoconfigure.rocketmq.RocketMQTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RocketMQTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                UserService.class,
                UserService.UserRegisterTransactionListener.class
        })
)
@Import({
        IdGeneratorConfig.class,
        MessageCodecConfig.class
})
class UserServiceMessageTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @SpyBean
    private UserService.UserRegisterTransactionListener registerTransactionListener;

    @Test
    void register() throws UsernameAlreadyExistException, InterruptedException {
        //成功
        AtomicReference<Boolean> done = new AtomicReference<>();

        when(userDao.insert(any())).thenAnswer(invocation -> {
            UserDO userDO = invocation.getArgument(0);
            try {
                assertTrue(userDO.getId() > 0);
                assertEquals(userDO.getUsername(), "yuyuko");
                assertEquals(userDO.getPassword(), "123456");
                done.set(true);
            } catch (Throwable ex) {
                done.set(false);
                System.err.println(ex);
            }
            return null;
        });

        userService.register(new LoginParam("yuyuko", "123456"));

        while (done.get() == null)
            TimeUnit.MILLISECONDS.sleep(500);

        assertTrue(done.get() != null && done.get());

        //主键冲突回滚
        reset(userDao);

        when(userDao.insert(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UsernameAlreadyExistException.class,
                () -> userService.register(new LoginParam("yuyuko", "123456"))
        );

        done.set(null);
        reset(userDao);

        //其他异常，插入失败

        when(userDao.insert(any())).thenThrow(RuntimeException.class);
        when(userDao.existById(anyLong())).thenReturn(Optional.of(false));
        when(registerTransactionListener.checkLocalTransaction(any())).thenAnswer(invocation -> {
            RocketMQLocalTransactionState state = RocketMQLocalTransactionState.ROLLBACK;
            try {
                state =
                        ((RocketMQLocalTransactionState) invocation.callRealMethod());
                assertEquals(RocketMQLocalTransactionState.ROLLBACK, state);
                done.set(true);
            } catch (Throwable ex) {
                done.set(false);
                System.err.println(ex);
            }
            return state;
        });

        userService.register(new LoginParam("yuyuko", "123456"));

        while (done.get() == null)
            TimeUnit.MILLISECONDS.sleep(500);

        assertTrue(done.get() != null && done.get());

        reset(userDao);
        reset(registerTransactionListener);
        done.set(false);

        //其他异常，插入成功

        when(userDao.insert(any())).thenThrow(RuntimeException.class);
        when(userDao.existById(anyLong())).thenReturn(Optional.of(true));
        when(registerTransactionListener.checkLocalTransaction(any())).thenAnswer(invocation -> {
            RocketMQLocalTransactionState state = RocketMQLocalTransactionState.ROLLBACK;
            try {
                state =
                        ((RocketMQLocalTransactionState) invocation.callRealMethod());
                assertEquals(RocketMQLocalTransactionState.COMMIT, state);
                done.set(true);
            } catch (Throwable ex) {
                done.set(false);
                System.err.println(ex);
            }
            return state;
        });

        userService.register(new LoginParam("yuyuko", "123456"));

        while (done.get() == null)
            TimeUnit.MILLISECONDS.sleep(500);

        assertTrue(done.get() != null && done.get());
    }
}
