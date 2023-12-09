package com.wangchen.mapper;

import com.wangchen.pojo.CustomerLog;
import com.wangchen.pojo.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface CustomerLogMapper {

    void insertCustomerLog(CustomerLog customerLog);

    int getTotalLogsCount();

    List<CustomerLog> getLogsByPage(@Param("start") int start, @Param("pageSize") int pageSize);
}

