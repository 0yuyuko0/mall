package com.yuyuko.mall.admin.controller;

import com.yuyuko.mall.admin.exception.IncorrectUsernameOrPasswordException;
import com.yuyuko.mall.admin.exception.UsernameAlreadyExistException;
import com.yuyuko.mall.admin.exception.UsernameNotExistsException;
import com.yuyuko.mall.admin.param.LoginParam;
import com.yuyuko.mall.admin.service.UserService;
import com.yuyuko.mall.admin.utils.ValidUtils;
import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.admin.result.UserResult;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/")
public class LoginController {
    @Autowired
    UserService userService;

    @PostMapping(value = "login")
    public Object login(@Valid @RequestBody @NotNull LoginParam loginParam,
                        BindingResult bindingResult,
                        HttpServletRequest request) throws IncorrectUsernameOrPasswordException,
            UsernameNotExistsException {
        if (request.getSession(false) != null)
            return CommonResult.failed();
        if (!ValidUtils.isValidUsername(loginParam.getUsername()) || !ValidUtils.isValidPassword(loginParam.getPassword()))
            return UserResult.incorrectUsernameOrPassword();

        UserSessionInfo userSessionInfo = userService.login(loginParam);

        HttpSession session = request.getSession(true);
        session.setAttribute("userSessionInfo", userSessionInfo);

        return CommonResult.success().data(userSessionInfo);
    }

    @PostMapping("logout")
    public Object logout(HttpSession session) {
        session.invalidate();
        return CommonResult.success();
    }

    @PostMapping("register")
    public Object register(@Valid @RequestBody LoginParam loginParam,
                           BindingResult bindingResult) throws UsernameAlreadyExistException {
        if (!ValidUtils.isValidUsername(loginParam.getUsername()) || !ValidUtils.isValidPassword(loginParam.getPassword()))
            return UserResult.incorrectUsernameOrPassword();
        userService.register(loginParam);
        return CommonResult.success();
    }
}
