package com.yuyuko.mall.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDO {
    private Long id;

    private String username;

    private String password;

    private LocalDateTime timeCreate;
}
