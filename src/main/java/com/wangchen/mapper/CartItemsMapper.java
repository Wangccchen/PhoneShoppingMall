package com.wangchen.mapper;

import com.wangchen.pojo.CartItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartItemsMapper {

    void addProductToCart(CartItem cartItem);

    int getCartIDByUserID(int userID);
}
