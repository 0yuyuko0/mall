package com.yuyuko.mall.seller.product.service;

import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.message.MessageCodec;
import com.yuyuko.mall.product.api.ProductRemotingService;
import com.yuyuko.mall.product.message.ProductCreateMessage;
import com.yuyuko.mall.product.param.ProductCreateParam;
import com.yuyuko.mall.seller.api.SellerInfoRemotingService;
import com.yuyuko.mall.seller.dto.SellerShopSimpleInfoDTO;
import com.yuyuko.mall.seller.product.param.ProductPublishParam;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yuyuko
 * @since 2019-08-09
 */
@Service
public class SellerProductService {
    @Reference
    private ProductRemotingService productRemotingService;

    @Reference
    private SellerInfoRemotingService sellerInfoRemotingService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private MessageCodec messageCodec;


    public void publishProduct(Long sellerId, ProductPublishParam publishParam) {
        SellerShopSimpleInfoDTO shopSimpleInfoDTO =
                sellerInfoRemotingService.getSellerShopInfo(sellerId);

        ProductCreateParam createParam = buildProductCreateParam(sellerId,
                shopSimpleInfoDTO.getShopId(), publishParam);


        rocketMQTemplate.sendMessageInTransaction("tx-product", "product:create",
                MessageBuilder.withPayload(
                        messageCodec.encode(
                                buildProductCreateMessage(publishParam, shopSimpleInfoDTO,
                                        createParam.getId()))
                ).setHeader(RocketMQHeaders.KEYS, idGenerator.nextId())
                        .build(),
                createParam);
    }

    @RocketMQTransactionListener(txProducerGroup = "tx-product")
    private class ProductCreateListener implements RocketMQLocalTransactionListener {
        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            ProductCreateParam createParam = (ProductCreateParam) arg;
            persistProduct(createParam);
            return RocketMQLocalTransactionState.COMMIT;
        }

        private void persistProduct(ProductCreateParam createParam) {
            productRemotingService.createProduct(createParam);
        }

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            ProductCreateParam createParam =
                    messageCodec.decode((byte[]) msg.getPayload(),
                            ProductCreateParam.class);
            if (productRemotingService.exist(createParam.getId()))
                return RocketMQLocalTransactionState.COMMIT;
            else
                return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    private ProductCreateParam buildProductCreateParam(Long sellerId, Long shopId,
                                                       ProductPublishParam publishParam) {
        ProductCreateParam createParam = new ProductCreateParam();
        BeanUtils.copyProperties(publishParam, createParam);

        createParam.setId(idGenerator.nextId());
        createParam.setSellerId(sellerId);
        createParam.setShopId(shopId);
        return createParam;
    }

    private ProductCreateMessage buildProductCreateMessage(ProductPublishParam publishParam,
                                                           SellerShopSimpleInfoDTO sellerShopSimpleInfo,
                                                           Long productId) {
        ProductCreateMessage message = new ProductCreateMessage();
        BeanUtils.copyProperties(publishParam, message);
        message.setId(productId);
        message.setShopId(sellerShopSimpleInfo.getShopId());
        message.setShopName(sellerShopSimpleInfo.getShopName());
        message.setTimeCreate(LocalDateTime.now());
        return message;
    }
}
