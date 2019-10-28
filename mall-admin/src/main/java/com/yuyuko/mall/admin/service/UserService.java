package com.yuyuko.mall.admin.service;

import com.yuyuko.mall.admin.dao.UserDao;
import com.yuyuko.mall.admin.dto.UserDTO;
import com.yuyuko.mall.admin.entity.UserDO;
import com.yuyuko.mall.admin.exception.IncorrectUsernameOrPasswordException;
import com.yuyuko.mall.admin.exception.UsernameAlreadyExistException;
import com.yuyuko.mall.admin.exception.UsernameNotExistsException;
import com.yuyuko.mall.admin.message.UserRegisterMessage;
import com.yuyuko.mall.admin.param.LoginParam;
import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import com.yuyuko.mall.user.api.UserPersonalInfoRemotingService;
import com.yuyuko.mall.user.dto.UserPersonalInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserDao userDao;

    @Reference
    private UserPersonalInfoRemotingService userPersonalInfoRemotingService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public UserSessionInfo login(LoginParam loginParam) throws IncorrectUsernameOrPasswordException,
            UsernameNotExistsException {
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();

        UserDTO user = userDao.getUserByUsername(username);
        if (user == null)
            throw new UsernameNotExistsException();

        if (!password.equals(user.getPassword()))
            throw new IncorrectUsernameOrPasswordException();
        Long userId = user.getId();
        return convertToUserSessionInfo(userPersonalInfoRemotingService.getUserPersonalInfo(userId));
    }

    private UserSessionInfo convertToUserSessionInfo(UserPersonalInfoDTO userPersonalInfoDTO) {
        UserSessionInfo userSessionInfo = new UserSessionInfo();
        BeanUtils.copyProperties(userPersonalInfoDTO, userSessionInfo);
        return userSessionInfo;
    }

    @Autowired
    private MessageCodec messageCodec;

    @Autowired
    private IdGenerator idGenerator;

    public void register(LoginParam loginParam) throws UsernameAlreadyExistException {
        long userId = idGenerator.nextId();

        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction("tx-admin",
                "admin:register",
                MessageBuilder
                        .withPayload(messageCodec.encode(buildUserRegisterMessage(loginParam,
                                userId)))
                        .setHeader(RocketMQHeaders.KEYS, idGenerator.nextId())
                        .build(),
                loginParam
        );

        if (sendResult.getLocalTransactionState() == LocalTransactionState.ROLLBACK_MESSAGE) {
            throw new UsernameAlreadyExistException();
        }

        log.info("用户名[{}]注册成功", loginParam.getUsername());
    }

    private UserRegisterMessage buildUserRegisterMessage(LoginParam loginParam,
                                                         long userId) {
        return new UserRegisterMessage(userId,
                loginParam.getUsername());
    }

    @RocketMQTransactionListener(txProducerGroup = "tx-admin")
    public class UserRegisterTransactionListener implements RocketMQLocalTransactionListener {
        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            UserRegisterMessage registerMessage =
                    messageCodec.decode((byte[]) msg.getPayload(),
                            UserRegisterMessage.class);
            LoginParam loginParam = (LoginParam) arg;

            String username = loginParam.getUsername();
            String password = loginParam.getPassword();

            UserDO user = new UserDO()
                    .setId(registerMessage.getUserId())
                    .setUsername(username)
                    .setPassword(password);

            try {
                userDao.insert(user);
            } catch (DataIntegrityViolationException ignore) {
                return RocketMQLocalTransactionState.ROLLBACK;
            } catch (Throwable ex) {
                log.warn(ex.getMessage());
                return RocketMQLocalTransactionState.UNKNOWN;
            }

            return RocketMQLocalTransactionState.COMMIT;
        }

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            UserRegisterMessage registerMessage =
                    messageCodec.decode((byte[]) msg.getPayload(),
                            UserRegisterMessage.class);
            Long userId = registerMessage.getUserId();
            if (userDao.existById(userId).orElse(false))
                return RocketMQLocalTransactionState.COMMIT;
            else
                return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
}