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
        when(userDao.getUserByUsername("test1")).thenReturn(null);
        assertThrows(UsernameNotExistsException.class, () -> userService.login(new LoginParam(
                "test1", "123456")));

        when(userDao.getUserByUsername("test2")).thenReturn(new UserDTO(1L, "test2", "1234567"));
        assertThrows(IncorrectUsernameOrPasswordException.class,
                () -> userService.login(new LoginParam(
                        "test2", "123456")));

        when(remotingService.getUserPersonalInfo(anyLong()))
                .thenReturn(new UserPersonalInfoDTO(1L, "123", "456", "789", true));
        when(userDao.getUserByUsername("test3")).thenReturn(new UserDTO(1L, "test3", "1234"));
        assertEquals(new UserSessionInfo(1L, "123", "456", "789", true),
                userService.login(new LoginParam("test3", "1234")));
    }


    @Test
    void register() {
        TransactionSendResult res1 = new TransactionSendResult();
        res1.setLocalTransactionState(LocalTransactionState.ROLLBACK_MESSAGE);

        when(rocketMQTemplate.sendMessageInTransaction(anyString(), anyString(), any(), any()))
                .thenAnswer(invocation -> {
                    Message m = (Message) invocation.getArgument(2);
                    UserRegisterMessage registerMessage =
                            messageCodec.decode(((byte[]) m.getPayload()),
                                    UserRegisterMessage.class);
                    assertEquals("yuyuko", registerMessage.getUsername());
                    assertTrue(registerMessage.getUserId() > 0);
                    return res1;
                });


        LoginParam param = new LoginParam("yuyuko", "1234");
        assertThrows(UsernameAlreadyExistException.class,
                () -> userService.register(param));


        res1.setLocalTransactionState(LocalTransactionState.COMMIT_MESSAGE);

        assertDoesNotThrow(() -> userService.register(param));
    }

    @Nested
    class RegisterTransactionListenerTest {
        UserService.UserRegisterTransactionListener transactionListener;

        @BeforeEach
        void setUp() {
            transactionListener = userService.new UserRegisterTransactionListener();
        }

        @Test
        void executeTx() {
            when(userDao.insert(any())).thenReturn(1);
            RocketMQLocalTransactionState state =
                    transactionListener.executeLocalTransaction(
                            MessageBuilder.withPayload(messageCodec.encode(new UserRegisterMessage(1L,
                                    "test1"))).build(),
                            new LoginParam("test1", "123456")
                    );
            assertEquals(COMMIT, state);


            when(userDao.insert(any())).thenThrow(DataIntegrityViolationException.class);
            state =
                    transactionListener.executeLocalTransaction(
                            MessageBuilder.withPayload(messageCodec.encode(new UserRegisterMessage(1L,
                                    "test1"))).build(), new LoginParam("test1", "123456")
                    );
            assertEquals(ROLLBACK, state);

            reset(userDao);
            when(userDao.insert(any())).thenThrow(RuntimeException.class);
            state =
                    transactionListener.executeLocalTransaction(
                            MessageBuilder.withPayload(messageCodec.encode(new UserRegisterMessage(1L,
                                    "test1"))).build(), new LoginParam("test1", "123456")
                    );
            assertEquals(UNKNOWN, state);
        }

        @Test
        void checkTx() {
            Message<byte[]> message =
                    MessageBuilder.withPayload(messageCodec.encode(new UserRegisterMessage(1L,
                            "test1"))).build();

            when(userDao.existById(anyLong())).thenReturn(Optional.of(false));
            assertEquals(ROLLBACK,
                    transactionListener.checkLocalTransaction(
                            message));

            when(userDao.existById(anyLong())).thenReturn(Optional.of(true));
            assertEquals(COMMIT,
                    transactionListener.checkLocalTransaction(
                            message));
        }
    }
}