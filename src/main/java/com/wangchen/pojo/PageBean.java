package com.wangchen.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PageBean {
    private int pageNum;  // 当前页码
    private int pageSize;  // 每页显示的商品数量
    private int totalItems;  // 总商品数量
    private int totalPages;  // 总页数
    private List<Product> products;  // 当前页的商品列表
}
