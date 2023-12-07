package com.wangchen.annotations;

import com.alibaba.fastjson.JSONObject;
import com.wangchen.mapper.CustomerLogMapper;
import com.wangchen.pojo.CustomerLog;
import com.wangchen.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Aspect //切面类
public class LogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CustomerLogMapper customerLogMapper;

    @Around("@annotation(com.wangchen.annotations.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //操作人ID - 当前登录员工ID
        //获取请求头中的jwt令牌, 解析令牌
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        Integer operateUser = (Integer) claims.get("userid");

        //操作时间
        LocalDateTime operateTime = LocalDateTime.now();

        //操作类名
        String className = joinPoint.getTarget().getClass().getName();

        //操作方法名
        String methodName = joinPoint.getSignature().getName();

        long begin = System.currentTimeMillis();
        //调用原始目标方法运行
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        //操作耗时
        Long costTime = end - begin;

        String operateType = className + ": 执行了 : "+methodName+" 操作，操作耗时 : "+costTime;
        //记录操作日志
        CustomerLog customerLog= new CustomerLog(0,operateUser,operateType,operateTime);
        customerLogMapper.insertCustomerLog(customerLog);

        return result;
    }

}
