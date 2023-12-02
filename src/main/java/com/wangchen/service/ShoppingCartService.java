package com.wangchen.service;

import com.wangchen.pojo.CartItem;

public interface ShoppingCartService {

    void addProductToCart(CartItem cartItem);
    int getCartIDByUserID(int userID);
}

