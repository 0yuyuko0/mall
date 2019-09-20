package com.yuyuko.mall.user.dao;

import com.yuyuko.mall.user.dto.UserPersonalInfoDTO;
import com.yuyuko.mall.user.entity.UserPersonalInfoDO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPersonalInfoDao {
    UserPersonalInfoDTO getUserPersonalInfoByUserId(Long userId);

    int insert(UserPersonalInfoDO userPersonalInfoDO);
}
