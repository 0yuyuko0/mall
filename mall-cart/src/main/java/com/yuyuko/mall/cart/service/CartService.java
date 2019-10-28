package com.yuyuko.mall.cart.service;

import com.yuyuko.mall.cart.bo.CartItemBO;
import com.yuyuko.mall.cart.bo.ShopCartItemBO;
import com.yuyuko.mall.cart.dao.CartDao;
import com.yuyuko.mall.cart.dto.CartItemDTO;
import com.yuyuko.mall.cart.entity.CartItemDO;
import com.yuyuko.mall.cart.param.CartUpdateParam;
import com.yuyuko.mall.common.idgenerator.IdGenerator;
import com.yuyuko.mall.common.utils.CollectionUtils;
import com.yuyuko.mall.product.api.ProductRemotingService;
import com.yuyuko.mall.product.dto.CartItemProductDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    CartDao cartDao;

    @Reference
    private ProductRemotingService productRemotingService;

    @Autowired
    IdGenerator idGenerator;

    public List<ShopCartItemBO> getCart(Long userId) {
        List<CartItemDTO> cartItems = cartDao.getCart(userId);

        return buildCart(cartItems);
    }

    public List<ShopCartItemBO> buildCart(List<CartItemDTO> cartItems) {
        List<Long> productIds =
                cartItems.stream().map(CartItemDTO::getProductId).collect(Collectors.toList());

        List<CartItemProductDTO> cartItemProducts =
                productRemotingService.listCartItemProducts(productIds);

        Map<Long, CartItemProductDTO> cartItemProductMap =
                CollectionUtils.convertListToMap(cartItemProducts,
                        "id", CartItemProductDTO.class);

        List<ShopCartItemBO> shopCartItemBOList = new ArrayList<>();
        Map<Long, Integer> shopCartItemBOListIndexMap = new HashMap<>();

        cartItems.forEach(cartItem -> {
            Long shopId = cartItem.getShopId();
            String shopName = cartItem.getShopName();

            Integer shopCartItemBOListIndex = shopCartItemBOListIndexMap.computeIfAbsent(shopId,
                    k -> {
                        shopCartItemBOList.add(buildShopCartItemBO(shopId, shopName));
                        return shopCartItemBOList.size() - 1;
                    }
            );

            ShopCartItemBO shopCartItemBO = shopCartItemBOList.get(shopCartItemBOListIndex);

            CartItemBO cartItemBO =
                    buildCartItemBO(cartItem, cartItemProductMap.get(cartItem.getProductId()));

            shopCartItemBO.getCartItems().add(cartItemBO);
        });

        return shopCartItemBOList;
    }


    public void updateCartItem(Long userId, CartUpdateParam param) {
        CartItemDTO cartItem = cartDao.getCartItem(userId, param.getProductId());
        if (cartItem == null) {
            cartDao.addCartItem(buildCartItemDO(param, userId));
        } else {
            if (param.getCount() + cartItem.getCount() == 0) {
                cartDao.deleteCartItemById(cartItem.getId());
            } else if (param.getCount() > 0)
                cartDao.updateCartItemCount(cartItem.getId(), cartItem.getCount());
        }
    }

    private CartItemBO buildCartItemBO(CartItemDTO cartItem, CartItemProductDTO cartItemProduct) {
        CartItemBO cartItemBO = new CartItemBO();
        BeanUtils.copyProperties(cartItem, cartItemBO);
        cartItemBO.setProduct(cartItemProduct);
        return cartItemBO;
    }

    private ShopCartItemBO buildShopCartItemBO(Long shopId, String shopName) {
        return new ShopCartItemBO()
                .setShopId(shopId)
                .setShopName(shopName)
                .setCartItems(new ArrayList<>());
    }

    private CartItemDO buildCartItemDO(CartUpdateParam addParam, Long userId) {
        CartItemDO cartItemDO = new CartItemDO();
        BeanUtils.copyProperties(addParam, cartItemDO);
        cartItemDO.setId(idGenerator.nextId());
        cartItemDO.setUserId(userId);
        return cartItemDO;
    }


    public void deleteCartItem(Long cartItemId) {
        cartDao.deleteCartItemById(cartItemId);
    }
}