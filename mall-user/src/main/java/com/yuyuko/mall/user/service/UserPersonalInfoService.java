package com.yuyuko.mall.user.service;

import com.yuyuko.mall.admin.message.UserRegisterMessage;
import com.yuyuko.mall.common.utils.ProtoStuffUtils;
import com.yuyuko.mall.common.utils.SnowflakeIdGenerator;
import com.yuyuko.mall.user.dao.UserPersonalInfoDao;
import com.yuyuko.mall.user.dto.UserPersonalInfoDTO;
import com.yuyuko.mall.user.entity.UserPersonalInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserPersonalInfoService {
    @Autowired
    UserPersonalInfoDao userPersonalInfoDao;

    public UserPersonalInfoDTO getUserPersonalInfoByUserId(Long userId) {
        return userPersonalInfoDao.getUserPersonalInfoByUserId(userId);
    }

    @RocketMQMessageListener(
            consumerGroup = "user-admin",
            topic = "admin",
            selectorExpression = "register"
    )
    @Service
    @Slf4j
    public static class UserRegisterListener implements RocketMQListener<MessageExt> {
        @Autowired
        UserPersonalInfoDao userPersonalInfoDao;

        @Autowired
        SnowflakeIdGenerator snowflakeIdGenerator;

        @Override
        public void onMessage(MessageExt message) {
            UserRegisterMessage registerMessage =
                    ProtoStuffUtils.deserialize(message.getBody(),
                            UserRegisterMessage.class);
            UserPersonalInfoDO userPersonalInfoDO = buildUserPersonalInfoDO(registerMessage);
            try {
                userPersonalInfoDao.insert(userPersonalInfoDO);
            } catch (DataIntegrityViolationException ex) {
                log.warn(ex.getLocalizedMessage());
            }
        }

        private UserPersonalInfoDO buildUserPersonalInfoDO(UserRegisterMessage registerMessage) {
            UserPersonalInfoDO userPersonalInfoDO = new UserPersonalInfoDO();
            BeanUtils.copyProperties(registerMessage, userPersonalInfoDO);
            userPersonalInfoDO.setId(snowflakeIdGenerator.nextId());
            return userPersonalInfoDO;
        }
    }
}
