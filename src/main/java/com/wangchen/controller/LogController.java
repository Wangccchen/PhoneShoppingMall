package com.wangchen.controller;

import com.wangchen.pojo.PageBean;
import com.wangchen.pojo.Result;
import com.wangchen.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LogController {
    @Autowired
    private LogService logService;
    @GetMapping("/backsystem/logs")
    public Result getLogsByPage(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageBean pageBean = logService.getLogsByPage(currentPage, pageSize);
        return Result.success(pageBean);
    }
}
