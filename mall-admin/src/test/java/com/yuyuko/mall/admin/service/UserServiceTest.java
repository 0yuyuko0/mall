package com.yuyuko.mall.admin.service;

import com.yuyuko.mall.admin.dao.UserDao;
import com.yuyuko.mall.admin.dto.UserDTO;
import com.yuyuko.mall.admin.exception.IncorrectUsernameOrPasswordException;
import com.yuyuko.mall.admin.exception.UsernameAlreadyExistException;
import com.yuyuko.mall.admin.exception.UsernameNotExistsException;
import com.yuyuko.mall.admin.message.UserRegisterMessage;
import com.yuyuko.mall.admin.param.LoginParam;
import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.idgenerator.SnowflakeIdGenerator;
import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.common.message.ProtostuffMessageCodec;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import com.yuyuko.mall.user.api.UserPersonalInfoRemotingService;
import com.yuyuko.mall.user.dto.UserPersonalInfoDTO;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.rocketmq.spring.core.RocketMQLocalTransactionState.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserDao userDao;

    @Mock
    private UserPersonalInfoRemotingService remotingService;

    @Mock
    private RocketMQTemplate rocketMQTemplate;

    @Spy
    private IdGenerator idGenerator = new SnowflakeIdGenerator(0, 0);

    @Spy
    private MessageCodec messageCodec = new ProtostuffMessageCodec();

    @Test
    void login() throws IncorrectUsernameOrPasswordException,
            UsernameNotExistsException {
        //Not Found
        when(userDao.getUserByUsername("test1")).thenReturn(null);
        assertThrows(UsernameNotExistsException.class, () -> userService.login(new LoginParam(
                "test1", "123456")));

        //wrong password
        when(userDao.getUserByUsername("test2")).thenReturn(new UserDTO(1L, "test2", "1234567"));
        assertThrows(IncorrectUsernameOrPasswordException.class,
                () -> userService.login(new LoginParam(
                        "test2", "123456")));

        //success
        when(remotingService.getUserPersonalInfo(anyLong()))
                .thenReturn(new UserPersonalInfoDTO(1L, "123", "456", "789", true));
        when(userDao.getUserByUsername("test3")).thenReturn(new UserDTO(1L, "test3", "1234"));
        assertEquals(new UserSessionInfo(1L, "123", "456", "789", true),
                userService.login(new LoginParam("test3", "1234")));
    }
}