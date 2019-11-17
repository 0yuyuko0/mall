package com.yuyuko.mall.search.order.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@ApiModel
@Accessors(chain = true)
public class OrderSearchParam {
    @ApiModelProperty(value = "搜索关键字", position = 1, required = true)
    private String keyword;

    @ApiModelProperty(value = "订单id", position = 2)
    private Long orderId;

    @ApiModelProperty(value = "商品id", position = 3)
    private Long productId;

    @ApiModelProperty(value = "订单状态", position = 4)
    private Integer status;

    @ApiModelProperty(value = "时间区间", position = 5)
    private TimeRange timeRange;

    @Range(max = 100)
    @ApiModelProperty(value = "搜索页数", position = 6)
    int page = 0;

    @Data
    @ApiModel
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimeRange {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @ApiModelProperty(value = "起始时间，格式如下 \"yyyy-MM-dd'T'HH:mm:ss\"，举例：2000-10-31T01:30:00", position = 1)
        private String from;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @ApiModelProperty(value = "终止时间", position = 2)
        private String to;
    }
}
