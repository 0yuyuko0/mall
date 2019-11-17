package com.yuyuko.mall.user.api.impl;

import com.yuyuko.mall.test.autoconfigure.dubbo.DubboTest;
import com.yuyuko.mall.user.api.UserPersonalInfoRemotingService;
import com.yuyuko.mall.user.dao.UserPersonalInfoDao;
import com.yuyuko.mall.user.dto.UserPersonalInfoDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DubboTest
class UserPersonalInfoRemotingServiceImplTest {
    @Reference
    private UserPersonalInfoRemotingService service;

    @MockBean
    private UserPersonalInfoDao userPersonalInfoDao;

    @Test
    void getUserPersonalInfo() {
        when(userPersonalInfoDao.getUserPersonalInfoByUserId(anyLong())).thenReturn(new UserPersonalInfoDTO());

        assertNotNull(service.getUserPersonalInfo(1L));
    }
}