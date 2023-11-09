package com.wangchen.service;

import com.wangchen.pojo.PageBean;
import com.wangchen.pojo.Product;

import java.util.List;

public interface ProductService {
    void addProduct(Product product);
    void deleteProduct(int productID);
    List<Product> getAllProducts();
    void updateProduct(Product product);
    List<Product> searchProductsByName(String keyword);
    PageBean getProductsByPage(int currentPage, int pageSize);
}
