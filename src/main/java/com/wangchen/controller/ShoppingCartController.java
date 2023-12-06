
package com.wangchen.controller;

import com.wangchen.pojo.CartItem;
import com.wangchen.pojo.CheckoutItem;
import com.wangchen.pojo.Product;
import com.wangchen.pojo.Result;
import com.wangchen.service.ProductService;
import com.wangchen.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    @Autowired
    private ProductService productService;

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

    @GetMapping("/get-products/{cartId}")
    public Result getProductsByCartId(@PathVariable Integer cartId) {
        try {
            List<Product> products = shoppingCartService.getProductsByCartId(cartId);
            return Result.success(products);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Error");
        }
    }

    @GetMapping("/get-quantity/{cartID}/{productID}")
    public Result getQuantityByCartIdAndProductId(@PathVariable Integer cartID, @PathVariable Integer productID) {
        try {
            Map<String, Object> params = Map.of("cartID", cartID, "productID", productID);
            Integer quantity = shoppingCartService.getQuantityByCartIdAndProductId(params);
            return Result.success(quantity);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Error");
        }
    }

    @PostMapping("/updateQuantity")
    public Result updateCartItemQuantity(@RequestBody CartItem request) {
        try {
            shoppingCartService.updateCartItemQuantity(request.getCartID(), request.getProductID(), request.getQuantity());
            return Result.success();
        } catch (Exception e) {
            return Result.error("Failed to update cart item quantity.");
        }
    }

    @DeleteMapping("/deleteCartItem/{cartID}/{productID}")
    public Result deleteCartItem(@PathVariable int cartID, @PathVariable int productID) {
        try {
            shoppingCartService.deleteCartItem(cartID, productID);
            return Result.success();
        } catch (Exception e) {
            return Result.error("Failed to delete cart item.");
        }
    }

    @GetMapping("/checkout/{userId}")
    public Result checkout(@PathVariable int userId) {
        try {

            // 获取用户购物车ID
            int cartId = shoppingCartService.getCartIDByUserID(userId);

            // 获取购物车中的商品列表
            List<Product> products = shoppingCartService.getProductsByCartId(cartId);
            // 计算总价
            BigDecimal totalPrice = BigDecimal.ZERO;
            List<Map<String, Object>> productsWithQuantity = new ArrayList<>();

            for (Product product : products) {
                BigDecimal productPrice = product.getPrice();
                Map<String, Object> params = new HashMap<>();
                params.put("cartID", cartId);
                params.put("productID", product.getProductID());
                System.out.println(params);
                int quantity = shoppingCartService.getQuantityByCartIdAndProductId(params);

                BigDecimal productTotalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));
                totalPrice = totalPrice.add(productTotalPrice);
                System.out.println(totalPrice);
                // 构建每个商品的数据，包括商品信息和数量
                Map<String, Object> productData = new HashMap<>();
                productData.put("product", product);
                productData.put("quantity", quantity);

                productsWithQuantity.add(productData);
            }

            // 构建返回的数据，包括商品列表和总价
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("products", productsWithQuantity);
            resultData.put("totalPrice", totalPrice);

            return Result.success(resultData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Failed to get checkout information.");
        }
    }


}

