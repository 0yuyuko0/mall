package com.yuyuko.mall.seller.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SellerInfoDO implements Serializable {
    private Long id;

    private Long userId;

    private Long shopId;

    private String shopName;

    private Integer waitPayCount;

    private Integer waitSendCount;

    private Integer waitRefundCount;

    private Integer waitRateCount;

    private LocalDateTime timeCreate;


}
