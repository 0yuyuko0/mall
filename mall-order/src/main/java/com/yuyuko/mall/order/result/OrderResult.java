package com.yuyuko.mall.order.result;

import com.yuyuko.mall.common.result.Result;

public class OrderResult {
    public static final int STOCK_NOT_ENOUGH = 702;

    public static final int WRONG_ORDER_STATUS = 703;

    public static final int NOT_ORDER_OWNER = 704;

    public static <T> Result<T> stockNotEnough() {
        return new Result<>(STOCK_NOT_ENOUGH,"商品库存不足");
    }
    public static <T> Result<T> wrongOrderStatus() {
        return new Result<>(WRONG_ORDER_STATUS,"订单状态不正确");
    }
    public static <T> Result<T> notOrderOwner() {
        return new Result<>(NOT_ORDER_OWNER,"该订单不属于该用户");
    }
}
