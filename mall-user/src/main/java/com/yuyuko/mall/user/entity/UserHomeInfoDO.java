package com.yuyuko.mall.user.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class UserHomeInfoDO {
    private Long id;

    private Long userId;

    private Integer likeProductCount;

    private Integer likeShopCount;

    private Integer browseHistoryCount;

    private Integer cartItemCount;

    private Integer waitPayCount;

    private Integer waitSendCount;

    private Integer waitReceiveCount;

    private Integer waitRateCount;

    private LocalDateTime timeCreate;
}
