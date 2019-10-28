package com.yuyuko.mall.user.dao;

import com.yuyuko.mall.user.dto.UserPersonalInfoDTO;
import com.yuyuko.mall.user.entity.UserPersonalInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserPersonalInfoDao {
    UserPersonalInfoDTO getUserPersonalInfoByUserId(Long userId);

    int insert(UserPersonalInfoDO userPersonalInfoDO);
}
