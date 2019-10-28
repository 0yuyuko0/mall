package com.yuyuko.mall.search.product.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class ProductSearchSort {
    public static final int GENERAL = 0;

    public static final int SALES = 1;

    public static final int COMMENT_COUNT = 2;

    public static final int TIME_CREATE = 3;

    public static final int PRICE_LOWER = 5;

    public static final int PRICE_HIGHER = 4;
}
