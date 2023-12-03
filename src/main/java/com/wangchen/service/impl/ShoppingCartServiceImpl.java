package com.wangchen.service.impl;

import com.wangchen.mapper.CartItemsMapper;
import com.wangchen.mapper.ProductMapper;
import com.wangchen.pojo.CartItem;
import com.wangchen.pojo.Product;
import com.wangchen.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CartItemsMapper cartItemsMapper;
    private final ProductMapper productMapper;
    @Autowired
    public ShoppingCartServiceImpl(CartItemsMapper cartItemsMapper, ProductMapper productMapper) {
        this.cartItemsMapper = cartItemsMapper;
        this.productMapper = productMapper;
    }

    @Override
    public void addProductToCart(CartItem cartItem) {
        cartItemsMapper.addProductToCart(cartItem);
    }

    @Override
    public int getCartIDByUserID(int userID) {
        return cartItemsMapper.getCartIDByUserID(userID);
    }

    @Override
    public List<Product> getProductsByCartId(Integer cartId) {
        List<Integer> productIds = cartItemsMapper.getProductIdsByCartId(cartId);
        return productMapper.getProductsByProductIds(productIds);
    }
    @Override
    public Integer getQuantityByCartIdAndProductId(Map<String, Object> params) {
        return cartItemsMapper.getQuantityByCartIdAndProductId(params);
    }

    @Override
    public void updateCartItemQuantity(int cartID, int productID, int newQuantity) {
        CartItem cartItem = new CartItem();
        cartItem.setCartID(cartID);
        cartItem.setProductID(productID);
        cartItem.setQuantity(newQuantity);
        cartItemsMapper.updateCartItemQuantity(cartItem);
    }

    @Override
    public void deleteCartItem(int cartID, int productID) {
        cartItemsMapper.deleteCartItem(cartID, productID);
        // 还可以添加其他逻辑，例如刷新购物车列表等
    }
}
