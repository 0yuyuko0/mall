package com.yuyuko.mall.user.service;

import com.yuyuko.mall.admin.message.UserRegisterMessage;
import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.idgenerator.SnowflakeIdGenerator;
import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.common.message.ProtostuffMessageCodec;
import com.yuyuko.mall.user.dao.UserHomeInfoDao;
import com.yuyuko.mall.user.dao.UserPersonalInfoDao;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserHomeInfoDao userHomeInfoDao;

    @Mock
    private UserPersonalInfoDao userPersonalInfoDao;

    @Test
    void getUserHomeInfo() {
        userService.getUserHomeInfo(1L);
    }

    @Nested
    class UserRegisterListenerTest {
        UserService.UserRegisterListener userRegisterListener;

        @BeforeEach
        void setUp() {
            userRegisterListener = userService.new UserRegisterListener();
            ReflectionTestUtils.setField(userRegisterListener, "messageCodec", messageCodec);
            ReflectionTestUtils.setField(userRegisterListener, "idGenerator", idGenerator);
        }

        @Spy
        private MessageCodec messageCodec = new ProtostuffMessageCodec();

        @Spy
        private IdGenerator idGenerator = new SnowflakeIdGenerator(0, 0);

        @Test
        void onRegisterMessage() throws NoSuchMethodException {
            MessageExt messageExt = new MessageExt();
            messageExt.setBody(messageCodec.encode(new UserRegisterMessage(1L, "yuyuko")));

            Method method = AopContext.class.getDeclaredMethod("setCurrentProxy",
                    Object.class);
            method.setAccessible(true);
            ReflectionUtils.invokeMethod(method, null, userRegisterListener);

            userRegisterListener.onMessage(messageExt);


            when(userPersonalInfoDao.insert(any())).thenThrow(DataIntegrityViolationException.class);
            userRegisterListener.onMessage(messageExt);

        }
    }
}