package com.yuyuko.mall.user.api.impl;

import com.yuyuko.mall.user.api.UserPersonalInfoRemotingService;
import com.yuyuko.mall.user.dao.UserPersonalInfoDao;
import com.yuyuko.mall.user.dto.UserPersonalInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class UserPersonalInfoRemotingServiceImpl implements UserPersonalInfoRemotingService {
    @Autowired
    private UserPersonalInfoDao userPersonalInfoDao;

    @Override
    public UserPersonalInfoDTO getUserPersonalInfo(Long userId) {
        return userPersonalInfoDao.getUserPersonalInfoByUserId(userId);
    }
}
