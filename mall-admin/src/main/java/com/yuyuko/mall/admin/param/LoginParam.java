package com.yuyuko.mall.admin.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginParam {
    @NotNull
    @Length(max = 16)
    private String username;

    @NotNull
    @Length(max = 16)
    private String password;

}
