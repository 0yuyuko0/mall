package com.yuyuko.mall.user.api;

import com.yuyuko.mall.user.dto.UserPersonalInfoDTO;

import javax.validation.constraints.NotNull;

public interface UserPersonalInfoRemotingService {
    UserPersonalInfoDTO getUserPersonalInfo(@NotNull Long userId);
}