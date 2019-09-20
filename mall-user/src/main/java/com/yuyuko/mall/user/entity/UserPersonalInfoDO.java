package com.yuyuko.mall.user.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserPersonalInfoDO {
    private Long id;

    private Long userId;

    private String username;

    private String nickname;

    private String avatar;

    private Boolean isSeller;

    private LocalDateTime timeCreate;
}
