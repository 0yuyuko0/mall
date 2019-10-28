package com.yuyuko.mall.admin.controller;

import com.yuyuko.mall.admin.exception.IncorrectUsernameOrPasswordException;
import com.yuyuko.mall.admin.exception.UsernameAlreadyExistException;
import com.yuyuko.mall.admin.exception.UsernameNotExistsException;
import com.yuyuko.mall.admin.param.LoginParam;
import com.yuyuko.mall.admin.service.UserService;
import com.yuyuko.mall.admin.utils.AdminValidUtils;
import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.admin.result.UserResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.yuyuko.mall.admin.result.UserResult.*;
import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;

@Api(value = "用户登录", tags = "Login")
@RestController
@RequestMapping("/")
public class LoginController {
    @Autowired
    UserService userService;

    @ApiOperation(value = "登录")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "登录成功，返回用户信息", response =
                    UserSessionInfo.class),
            @ApiResponse(code = INCORRECT_USERNAME_OR_PASSWORD, message = "用户名密码不正确"),
            @ApiResponse(code = USERNAME_NOT_EXISTS, message = "用户未注册"),
    })
    @PostMapping(value = "login")
    public Result<?> login(@Valid @RequestBody @NotNull LoginParam loginParam,
                           BindingResult bindingResult,
                           HttpServletRequest request) throws IncorrectUsernameOrPasswordException,
            UsernameNotExistsException {
        if (request.getSession(false) != null)
            return CommonResult.failed();
        if (!AdminValidUtils.isValidUsername(loginParam.getUsername()) || !AdminValidUtils.isValidPassword(loginParam.getPassword()))
            return UserResult.incorrectUsernameOrPassword();

        UserSessionInfo userSessionInfo = userService.login(loginParam);

        HttpSession session = request.getSession(true);
        session.setAttribute("userSessionInfo", userSessionInfo);

        return CommonResult.success().data(userSessionInfo);
    }

    @PostMapping("logout")
    @ApiOperation(value = "注销")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "成功"),
    })
    public Result<?> logout(@ApiIgnore HttpSession session) {
        session.invalidate();
        return CommonResult.success();
    }

    @ApiOperation(value = "注册")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "成功"),
            @ApiResponse(code = USERNAME_ALREADY_EXISTS, message = "用户名已被注册"),
    })
    @PostMapping("register")
    public Result<?> register(@Valid @RequestBody LoginParam loginParam,
                              BindingResult bindingResult) throws UsernameAlreadyExistException {
        if (!AdminValidUtils.isValidUsername(loginParam.getUsername()) || !AdminValidUtils.isValidPassword(loginParam.getPassword()))
            return UserResult.incorrectUsernameOrPassword();
        userService.register(loginParam);
        return CommonResult.success();
    }
}
