package com.wangchen.mapper;

import com.wangchen.pojo.CustomerLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface CustomerLogMapper {

    void insertCustomerLog(CustomerLog customerLog);
}

