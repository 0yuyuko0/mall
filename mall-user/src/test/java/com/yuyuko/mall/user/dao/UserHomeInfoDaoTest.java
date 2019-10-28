package com.yuyuko.mall.user.dao;

import com.yuyuko.mall.user.entity.UserHomeInfoDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserHomeInfoDaoTest {
    @Autowired
    private UserHomeInfoDao userHomeInfoDao;

    @BeforeEach
    public void insertTestData() {
        UserHomeInfoDO userHomeInfoDO = new UserHomeInfoDO();
        userHomeInfoDO.setId(-1L);
        userHomeInfoDO.setUserId(-2L);
        userHomeInfoDao.insert(userHomeInfoDO);
    }

    @Test
    void getUserHomeInfo() {
        assertNotNull(userHomeInfoDao.getUserHomeInfo(-2L));
    }
}