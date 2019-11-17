package com.yuyuko.mall.user.service;

import com.yuyuko.idempotent.annotation.Idempotent;
import com.yuyuko.mall.admin.message.UserRegisterMessage;
import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.user.dao.UserHomeInfoDao;
import com.yuyuko.mall.user.dao.UserPersonalInfoDao;
import com.yuyuko.mall.user.dto.UserHomeInfoDTO;
import com.yuyuko.mall.user.entity.UserHomeInfoDO;
import com.yuyuko.mall.user.entity.UserPersonalInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserHomeInfoDao userHomeInfoDao;

    @Autowired
    private UserPersonalInfoDao userPersonalInfoDao;

    public UserHomeInfoDTO getUserHomeInfo(Long id) {
        return userHomeInfoDao.getUserHomeInfo(id);
    }

    @RocketMQMessageListener(
            consumerGroup = "user-admin",
            topic = "admin",
            selectorExpression = "register"
    )
    @Service
    public class UserRegisterListener implements RocketMQListener<MessageExt> {

        @Autowired
        private MessageCodec messageCodec;

        @Autowired
        private IdGenerator idGenerator;

        @Override
        @Idempotent(id = "#message.getKeys()")
        public void onMessage(MessageExt message) {
            UserRegisterMessage registerMessage =
                    messageCodec.decode(message.getBody(),
                            UserRegisterMessage.class);
            try {
                ((UserRegisterListener) AopContext.currentProxy()).onRegister(registerMessage);
            } catch (DataIntegrityViolationException ex) {//幂等
                log.warn(ex.getLocalizedMessage());
            }
        }

        @Transactional
        void onRegister(UserRegisterMessage registerMessage) {
            UserPersonalInfoDO userPersonalInfoDO = buildUserPersonalInfoDO(registerMessage);
            UserHomeInfoDO userHomeInfoDO = buildUserHomeInfoDO(registerMessage);
            userPersonalInfoDao.insert(userPersonalInfoDO);
            userHomeInfoDao.insert(userHomeInfoDO);
        }

        private UserHomeInfoDO buildUserHomeInfoDO(UserRegisterMessage registerMessage) {
            UserHomeInfoDO userHomeInfoDO = new UserHomeInfoDO();
            userHomeInfoDO.setId(idGenerator.nextId());
            userHomeInfoDO.setUserId(registerMessage.getUserId());
            return userHomeInfoDO;
        }

        private UserPersonalInfoDO buildUserPersonalInfoDO(UserRegisterMessage registerMessage) {
            UserPersonalInfoDO userPersonalInfoDO = new UserPersonalInfoDO();
            BeanUtils.copyProperties(registerMessage, userPersonalInfoDO);
            userPersonalInfoDO.setId(idGenerator.nextId());
            return userPersonalInfoDO;
        }
    }
}
