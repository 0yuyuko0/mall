package com.yuyuko.mall.admin.dao;

import com.yuyuko.mall.admin.entity.UserDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void insertTestData() {
        userDao.insert(new UserDO((long) -1, "test1", "123456", LocalDateTime.now()));
    }


    @Test
    void getUserByUsername() {
        assertNotNull(userDao.getUserByUsername("test1"));
        assertNull(userDao.getUserByUsername("test2"));
    }

    @Test
    void existById() {
        assertTrue(userDao.existById(-1L).orElse(false));
        assertFalse(userDao.existById(-2L).orElse(false));
    }
}