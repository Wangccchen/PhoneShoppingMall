package com.wangchen.controller;

import com.wangchen.pojo.PageBean;
import com.wangchen.pojo.Product;
import com.wangchen.pojo.Result;
import com.wangchen.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    //商品页默认展示分页后的商品
    @GetMapping
    public Result getProductsByPage(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageBean pageBean = productService.getProductsByPage(currentPage, pageSize);
        return Result.success(pageBean);
    }

    @GetMapping("/{productID}")
    public Result getProductByID(@PathVariable("productID") int productID){
        System.out.println(productID);
        Product product = productService.getProductByID(productID);
        return Result.success(product);
    }

    //商品页获取所有商品的信息
    @GetMapping("/allProducts")
    public Result getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return Result.success(products);
    }

    @PostMapping
    public Result addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return Result.success("Product added successfully");
    }

    @DeleteMapping("/delete/{productID}")
    public Result deleteProduct(@PathVariable("productID") int productID) {
        productService.deleteProduct(productID);
        return Result.success("Product deleted successfully");
    }

    @PostMapping("/deleteBatch")
    public Result deleteProductsBatch(@RequestBody List<Integer> productIds) {
        try {
            productService.deleteProductsByIds(productIds);
            return Result.success("批量删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/search")
    public Result searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProductsByName(keyword);
        return Result.success(products);
    }

    //编辑商品请求
    @PutMapping
    public Result updateProducts(@RequestBody Product product){
        productService.updateProduct(product);
        return Result.success();
    }
}
