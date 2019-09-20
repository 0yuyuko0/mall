package com.yuyuko.mall.user.api.impl;

import com.yuyuko.mall.common.exception.WrappedException;
import com.yuyuko.mall.user.api.UserPersonalInfoService;
import com.yuyuko.mall.user.dto.UserPersonalInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class UserPersonalInfoServiceImpl implements UserPersonalInfoService {
    @Autowired
    com.yuyuko.mall.user.service.UserPersonalInfoService userPersonalInfoService;

    @Override
    public UserPersonalInfoDTO getUserPersonalInfo(Long userId){
        try {
            return userPersonalInfoService.getUserPersonalInfoByUserId(userId);
        }catch (RuntimeException ex){
            log.error(ex.getMessage());
            throw new WrappedException(ex);
        }
    }
}
