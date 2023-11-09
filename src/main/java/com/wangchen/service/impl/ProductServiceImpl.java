package com.wangchen.service.impl;

import com.wangchen.mapper.ProductMapper;
import com.wangchen.pojo.PageBean;
import com.wangchen.pojo.Product;
import com.wangchen.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    //添加商品
    @Override
    public void addProduct(Product product) {
        productMapper.addProduct(product);
    }

    //删除商品
    @Override
    public void deleteProduct(int productID) {
        productMapper.deleteProduct(productID);
    }

    //显示全部商品
    @Override
    public List<Product> getAllProducts() {
        return productMapper.getAllProducts();
    }

    //修改商品信息
    @Override
    public void updateProduct(Product product) {
        productMapper.updateProduct(product);
    }

    //模糊搜索
    @Override
    public List<Product> searchProductsByName(String keyword) {
        return productMapper.searchProductsByName(keyword);
    }

    @Override
    public PageBean getProductsByPage(int currentPage, int pageSize) {
        List<Product> products = productMapper.getProductsByPage((currentPage - 1) * pageSize, pageSize);
        int totalItems = productMapper.getTotalProductsCount();  // 获取总商品数量
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);  // 计算总页数

        PageBean pageBean = new PageBean();
        pageBean.setPageNum(currentPage);
        pageBean.setPageSize(pageSize);
        pageBean.setTotalItems(totalItems);
        pageBean.setTotalPages(totalPages);
        pageBean.setProducts(products);

        return pageBean;
    }
}
