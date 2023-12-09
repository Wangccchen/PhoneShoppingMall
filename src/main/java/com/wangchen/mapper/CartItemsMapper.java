package com.wangchen.mapper;

import com.wangchen.pojo.CartItem;
import com.wangchen.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CartItemsMapper {

    void addProductToCart(CartItem cartItem);

    int getCartIDByUserID(int userID);
    List<Integer> getProductIdsByCartId(Integer cartId);

    Integer getQuantityByCartIdAndProductId(Map<String, Object> params);
    void updateCartItemQuantity(CartItem cartItem);
    void deleteCartItem(@Param("cartID") int cartID, @Param("productID") int productID);

    void deleteByCartId(@Param("cartID") int cartID);
}
