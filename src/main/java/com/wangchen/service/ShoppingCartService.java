package com.wangchen.service;

import com.wangchen.pojo.CartItem;
import com.wangchen.pojo.Product;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {

    void addProductToCart(CartItem cartItem);
    int getCartIDByUserID(int userID);
    List<Product> getProductsByCartId(Integer cartId);
    Integer getQuantityByCartIdAndProductId(Map<String, Object> params);

    public void updateCartItemQuantity(int cartID, int productID, int newQuantity);

    void deleteCartItem(int cartID, int productID);
}

