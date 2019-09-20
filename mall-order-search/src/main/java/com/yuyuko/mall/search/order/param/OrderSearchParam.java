package com.yuyuko.mall.search.order.param;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class OrderSearchParam {
    private String keyword;

    private Long orderId;

    private Long productId;

    private Integer status;

    private TimeCreateRange timeCreate;

    @Range(max = 100)
    int page = 0;

    @Data
    public static class TimeCreateRange {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime from;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime to;
    }
}
