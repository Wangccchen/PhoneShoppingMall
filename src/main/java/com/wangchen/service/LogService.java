package com.wangchen.service;

import com.wangchen.pojo.PageBean;

public interface LogService {
    PageBean getLogsByPage(int currentPage, int pageSize);



}
