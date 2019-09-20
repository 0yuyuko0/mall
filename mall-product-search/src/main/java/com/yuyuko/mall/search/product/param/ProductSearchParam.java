package com.yuyuko.mall.search.product.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class ProductSearchParam {
    @Length(max = 36)
    String keyword;

    @Range(min = 0, max = 5)
    int sort = 0;

    Long brandId;

    PriceRange price;

    @Range(max = 100)
    int page = 0;

    @Data
    public static class PriceRange{
        Integer from;

        Integer to;
    }
}