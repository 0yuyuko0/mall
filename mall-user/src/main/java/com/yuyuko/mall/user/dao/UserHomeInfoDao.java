package com.yuyuko.mall.user.dao;

import com.yuyuko.mall.user.dto.UserHomeInfoDTO;
import com.yuyuko.mall.user.entity.UserHomeInfoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserHomeInfoDao {
    UserHomeInfoDTO getUserHomeInfo(Long userId);

    int insert(UserHomeInfoDO userHomeInfoDO);
}
