package com.yuyuko.mall.user.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserPersonalInfoServiceTests {
    @Autowired
    UserPersonalInfoService userPersonalInfoService;

    @Test
    public void testGetInfo() {
        Assert.assertNotNull(userPersonalInfoService.getUserPersonalInfoByUserId(1L));
    }
}
