package com.yuyuko.mall.user.controller;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import com.yuyuko.mall.user.dto.UserHomeInfoDTO;
import com.yuyuko.mall.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import springfox.documentation.annotations.ApiIgnore;

import static com.yuyuko.mall.common.result.CommonResult.*;

@RestController
@RequestMapping("/")
@Api(tags = "User")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("home/get")
    @ApiOperation("获取用户主页信息")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = UserHomeInfoDTO.class),
    })
    public Result<?> getUserHomeInfo(@ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo) {
        return CommonResult.success().data(userService.getUserHomeInfo(userSessionInfo.getId()));
    }
}
