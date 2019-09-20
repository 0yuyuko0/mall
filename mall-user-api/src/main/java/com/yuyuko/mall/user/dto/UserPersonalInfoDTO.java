package com.yuyuko.mall.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPersonalInfoDTO implements Serializable {
    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private Boolean isSeller;
}