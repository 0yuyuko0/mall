package com.yuyuko.mall.session.pojo;

import lombok.Data;

@Data
public class UserSessionInfo {
    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private Boolean isSeller;
}
