package com.wangchen.service.impl;

import com.wangchen.mapper.CustomerLogMapper;
import com.wangchen.mapper.ProductMapper;
import com.wangchen.pojo.CustomerLog;
import com.wangchen.pojo.PageBean;
import com.wangchen.pojo.Product;
import com.wangchen.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private CustomerLogMapper customerLogMapper;
    @Override
    public PageBean getLogsByPage(int currentPage, int pageSize) {
        List<CustomerLog> logs = customerLogMapper.getLogsByPage((currentPage - 1) * pageSize, pageSize);
        int totalItems = customerLogMapper.getTotalLogsCount();  // 获取总商品数量
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);  // 计算总页数

        PageBean pageBean = new PageBean();
        pageBean.setPageNum(currentPage);
        pageBean.setPageSize(pageSize);
        pageBean.setTotalItems(totalItems);
        pageBean.setTotalPages(totalPages);
        pageBean.setLogs(logs);

        return pageBean;
    }
}
