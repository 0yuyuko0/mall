package com.yuyuko.mall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPersonalInfoDTO implements Serializable {
    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private Boolean isSeller;
}