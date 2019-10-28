package com.yuyuko.mall.user.dao;

import com.yuyuko.mall.user.entity.UserHomeInfoDO;
import com.yuyuko.mall.user.entity.UserPersonalInfoDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserPersonalInfoDaoTest {
    @Autowired
    private UserPersonalInfoDao userPersonalInfoDao;

    @BeforeEach
    public void insertTestData() {
        UserPersonalInfoDO userPersonalInfoDO = new UserPersonalInfoDO();
        userPersonalInfoDO.setId(-1L);
        userPersonalInfoDO.setUserId(-2L);
        userPersonalInfoDO.setUsername("yuyuko");
        userPersonalInfoDao.insert(userPersonalInfoDO);
    }

    @Test
    void getUserPersonalInfoByUserId() {
        assertNotNull(userPersonalInfoDao.getUserPersonalInfoByUserId(-2L));
    }
}