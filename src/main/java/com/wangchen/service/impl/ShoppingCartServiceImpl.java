package com.wangchen.service.impl;

import com.wangchen.mapper.CartItemsMapper;
import com.wangchen.pojo.CartItem;
import com.wangchen.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CartItemsMapper cartItemsMapper;

    @Autowired
    public ShoppingCartServiceImpl(CartItemsMapper cartItemsMapper) {
        this.cartItemsMapper = cartItemsMapper;
    }

    @Override
    public void addProductToCart(CartItem cartItem) {
        cartItemsMapper.addProductToCart(cartItem);
    }

    @Override
    public int getCartIDByUserID(int userID) {
        return cartItemsMapper.getCartIDByUserID(userID);
    }
}
