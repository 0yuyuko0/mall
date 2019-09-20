package com.yuyuko.mall.admin.service;

import com.yuyuko.mall.admin.dao.UserDao;
import com.yuyuko.mall.admin.exception.IncorrectUsernameOrPasswordException;
import com.yuyuko.mall.admin.exception.UsernameAlreadyExistException;
import com.yuyuko.mall.admin.exception.UsernameNotExistsException;
import com.yuyuko.mall.admin.param.LoginParam;
import com.yuyuko.mall.user.api.UserPersonalInfoService;
import com.yuyuko.mall.user.dto.UserPersonalInfoDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTests {
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Mock
    public UserPersonalInfoService userPersonalInfoService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(
                userService,
                "userPersonalInfoService",
                userPersonalInfoService);
        given(userPersonalInfoService.getUserPersonalInfo(anyLong())).willReturn(new UserPersonalInfoDTO());
    }

    @Test
    public void login() {
        LoginParam loginParam = new LoginParam();
        loginParam.setUsername("yuyuko");
        loginParam.setPassword("12345678");
        try {
            userService.login(loginParam);
        } catch (IncorrectUsernameOrPasswordException | UsernameNotExistsException ex) {

        }
    }

    @Test
    @Rollback
    public void register() {
        LoginParam loginParam = new LoginParam();
        loginParam.setUsername("yuyuko10");
        loginParam.setPassword("12345678");
        try {
            userService.register(loginParam);
        } catch (UsernameAlreadyExistException e) {
        }
    }
}
