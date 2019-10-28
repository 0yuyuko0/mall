package com.yuyuko.mall.search.order.controller;

import com.yuyuko.mall.common.result.CommonResult;
import com.yuyuko.mall.common.result.Result;
import com.yuyuko.mall.search.order.entity.Order;
import com.yuyuko.mall.search.order.param.OrderSearchParam;
import com.yuyuko.mall.search.order.service.OrderSearchService;
import com.yuyuko.mall.session.pojo.UserSessionInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

import static com.yuyuko.mall.common.result.CommonResult.SUCCESS;

@RestController
@RequestMapping("/")
@Api(tags = "SearchOrder")
public class OrderSearchController {
    @Autowired
    private OrderSearchService orderSearchService;

    private static class SearchResponse extends PageImpl<Order> {
        public SearchResponse(List<Order> content) {
            super(content);
        }
    }

    @GetMapping
    @ApiOperation("搜索订单")
    @ApiResponses({
            @ApiResponse(code = SUCCESS, message = "", response = SearchResponse.class)
    })
    public Result<?> searchOrders(@ModelAttribute @Valid OrderSearchParam orderSearchParam,
                                  @ApiIgnore @SessionAttribute UserSessionInfo userSessionInfo
    ) {
        return CommonResult.success().data(orderSearchService.searchOrders(userSessionInfo.getId(), orderSearchParam));
    }
}
