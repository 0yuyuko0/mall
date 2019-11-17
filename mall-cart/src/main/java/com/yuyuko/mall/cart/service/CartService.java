package com.yuyuko.mall.cart.service;

import com.yuyuko.mall.cart.bo.CartItemBO;
import com.yuyuko.mall.cart.bo.ShopCartItemBO;
import com.yuyuko.mall.cart.dao.CartDao;
import com.yuyuko.mall.cart.dto.CartItemDTO;
import com.yuyuko.mall.cart.entity.CartItemDO;
import com.yuyuko.mall.cart.param.CartAddParam;
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
    private CartDao cartDao;

    @Reference
    private ProductRemotingService productRemotingService;

    @Autowired
    private IdGenerator idGenerator;

    public List<ShopCartItemBO> getCart(Long userId) {
        List<CartItemDTO> cartItems = cartDao.getCart(userId);

        return buildCart(cartItems);
    }

    private List<ShopCartItemBO> buildCart(List<CartItemDTO> cartItems) {
        List<Long> productIds =
                cartItems.stream().map(CartItemDTO::getProductId).collect(Collectors.toList());

        List<CartItemProductDTO> cartItemProducts =
                productRemotingService.listCartItemProducts(productIds);

        Map<Long, CartItemProductDTO> cartItemProductMap =
                CollectionUtils.transListToMap(cartItemProducts,
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

    private CartItemDO buildCartItemDO(CartAddParam addParam, Long userId) {
        CartItemDO cartItemDO = new CartItemDO();
        BeanUtils.copyProperties(addParam, cartItemDO);
        cartItemDO.setId(idGenerator.nextId());
        cartItemDO.setUserId(userId);
        return cartItemDO;
    }


    public void deleteCartItem(Long cartItemId) {
        cartDao.deleteCartItemById(cartItemId);
    }

    public void addCartItem(Long userId, CartAddParam param) {
        CartItemDTO cartItem = cartDao.getCartItem(userId, param.getProductId());
        if (cartItem == null) {
            if (param.getShopId() == null || param.getShopName() == null)
                throw new RuntimeException("添加到购物车时请带上店铺id与店铺名");
            cartDao.insertCartItem(buildCartItemDO(param, userId));
        } else
            cartDao.addCartItemCount(cartItem.getId(), param.getCount());
    }

    public void reduceCartItem(long userId, long id) {
        cartDao.reduceCartItemByOne(userId, id);
    }

    public void updateCartItem(long userId, CartUpdateParam cartUpdateParam) {
        cartDao.updateCartItemCount(userId, cartUpdateParam.getId(),
                cartUpdateParam.getCount());
    }
}