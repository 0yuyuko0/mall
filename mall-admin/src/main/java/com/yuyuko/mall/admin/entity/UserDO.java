package com.yuyuko.mall.admin.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserDO {
    private Long id;

    private String username;

    private String password;

    private LocalDateTime timeCreate;
}
