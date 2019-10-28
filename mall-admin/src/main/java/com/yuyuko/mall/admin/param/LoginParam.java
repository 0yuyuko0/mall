package com.yuyuko.mall.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "登录表单")
@AllArgsConstructor
@NoArgsConstructor
public class LoginParam {
    @NotNull
    @Length(max = 16)
    @ApiModelProperty(value = "用户名：长度不超过16个字符，允许有字母、数字、汉字、下划线，一个汉字算两个字符", required = true,
            position = 1)
    private String username;

    @NotNull
    @Length(max = 16)
    @ApiModelProperty(value = "密码：长度不超过16个字符，不低于8个字符，不允许有汉字与空格", required = true,
            position = 2)
    private String password;

}
