package com.yuyuko.mall.search.product.service;

import com.yuyuko.idempotent.annotation.Idempotent;
import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.common.utils.ProtoStuffUtils;
import com.yuyuko.mall.product.message.ProductCreateMessage;
import com.yuyuko.mall.search.product.dao.ProductRepository;
import com.yuyuko.mall.search.product.entity.Product;
import com.yuyuko.mall.search.product.param.ProductSearchParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.elasticsearch.action.search.ClearScrollRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.yuyuko.mall.search.product.param.ProductSearchSort.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortBuilders.scoreSort;

@Service
public class ProductSearchService {

    @RocketMQMessageListener(
            consumerGroup = "search-product",
            topic = "product",
            selectorExpression = "create"
    )
    @Service
    @Slf4j
    public static class ProductCreateListener implements RocketMQListener<MessageExt> {
        @Autowired
        ProductRepository productRepository;

        @Autowired
        private MessageCodec messageCodec;

        @Override
        @Idempotent(id = "#message.getKeys()")
        public void onMessage(MessageExt message) {
            ProductCreateMessage createMessage = messageCodec.decode(message.getBody(),
                    ProductCreateMessage.class);
            Product product = new Product();
            BeanUtils.copyProperties(createMessage, product);
            if (!productRepository.existsById(createMessage.getId())) {
                productRepository.save(product);
            }
        }
    }


    @Autowired
    ProductRepository productRepository;

    private int productPageSize = 60;

    public Page<Product> searchProducts(ProductSearchParam productSearchParam) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        BoolQueryBuilder queryBuilder =
                boolQuery().must(
                        matchQuery("name", productSearchParam.getKeyword())
                );

        if (productSearchParam.getBrandId() != null)
            queryBuilder.filter(termQuery("brandId", productSearchParam.getBrandId()));

        if (productSearchParam.getPrice() != null) {
            RangeQueryBuilder rangeQueryBuilder = rangeQuery("price");
            ProductSearchParam.PriceRange priceRange = productSearchParam.getPrice();
            if (priceRange.getFrom() != null)
                rangeQueryBuilder.from(priceRange.getFrom());
            if (priceRange.getTo() != null)
                rangeQueryBuilder.to(priceRange.getTo());
            queryBuilder.filter(rangeQueryBuilder);
        }

        switch (productSearchParam.getSort()) {
            case SALES:
                builder.withSort(fieldSort("sales").order(SortOrder.DESC));
                break;
            case COMMENT_COUNT:
                builder.withSort(fieldSort("commentCount").order(SortOrder.DESC));
                break;
            case TIME_CREATE:
                builder.withSort(fieldSort("timeCreate").order(SortOrder.DESC));
                break;
            case PRICE_LOWER:
                builder.withSort(fieldSort("price").order(SortOrder.ASC));
                break;
            case PRICE_HIGHER:
                builder.withSort(fieldSort("price").order(SortOrder.DESC));
                break;
            case GENERAL:
                builder.withSort(scoreSort());
        }

        FieldValueFactorFunctionBuilder fieldValueFactorFunctionBuilder =
                new FieldValueFactorFunctionBuilder("sales");
        fieldValueFactorFunctionBuilder.factor(0.2f).modifier(FieldValueFactorFunction.Modifier.LOG1P);

        FunctionScoreQueryBuilder functionScoreQueryBuilder = functionScoreQuery(queryBuilder,
                fieldValueFactorFunctionBuilder);

        builder
                .withSearchType(SearchType.DEFAULT)
                .withQuery(functionScoreQueryBuilder)
                .withPageable(PageRequest.of(productSearchParam.getPage(), productPageSize));

        return productRepository.search(builder.build());
    }
}
