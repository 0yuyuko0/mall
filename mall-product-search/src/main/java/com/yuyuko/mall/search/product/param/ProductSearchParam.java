package com.yuyuko.mall.search.product.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
@ApiModel
public class ProductSearchParam {
    @ApiModelProperty(value = "搜索关键字", required = true, position = 1)
    @Length(max = 36)
    private String keyword;

    @ApiModelProperty(value =
            "搜索排序字段，共有六种取值，综合排序(0)，销量倒序(1)，评论倒序(2)" +
                    "，时间倒序(3)，价格倒序(4)，价格顺序(5)，默认综合排序",
            allowableValues = "0,1,2,3,4,5",
            position = 2
    )
    @Range(min = 0, max = 5)
    private int sort = 0;

    @ApiModelProperty(value = "品牌id，搜索特定品牌下商品时使用", position = 3)
    private Long brandId;

    @ApiModelProperty(value = "价格区间", position = 4)
    private PriceRange price;

    @Range(max = 100)
    @ApiModelProperty(value = "搜索页数", position = 5)
    private int page = 0;

    @Data
    public static class PriceRange {
        @ApiModelProperty(value = "价格左区间", position = 1)
        private Integer from;

        @ApiModelProperty(value = "价格右区间", position = 2)
        private Integer to;
    }
}