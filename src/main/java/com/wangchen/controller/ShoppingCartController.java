
package com.wangchen.controller;

import com.wangchen.pojo.CartItem;
import com.wangchen.pojo.Result;
import com.wangchen.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/addProduct")
    public Result addProductToCart(@RequestBody CartItem cartItem) {
        shoppingCartService.addProductToCart(cartItem);
        return Result.success("已加入购物车！");
    }

    @GetMapping("/get-cart-id/{userID}")
    public Result getCartIDByUserID(@PathVariable int userID) {
        int id = shoppingCartService.getCartIDByUserID(userID);
        return Result.success(id);
    }
}

