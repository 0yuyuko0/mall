package com.yuyuko.mall.admin.dao;

import com.yuyuko.mall.admin.dto.UserDTO;
import com.yuyuko.mall.admin.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao {
    UserDTO getUserByUsername(String username);

    int insert(UserDO userDO);

    Optional<Boolean> existById(Long userId);

    int deleteByUsername(String username);
}
