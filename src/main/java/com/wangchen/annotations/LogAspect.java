package com.wangchen.annotations;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangchen.mapper.CustomerLogMapper;
import com.wangchen.pojo.CustomerLog;
import com.wangchen.pojo.Product;
import com.wangchen.pojo.Result;
import com.wangchen.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@Component
@Aspect //切面类
public class LogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private CustomerLogMapper customerLogMapper;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper用于将对象转为JSON字符串
    private String parsePurchaseInfo(Object[] args) {
        // 根据实际情况解析购买商品信息
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg instanceof Map) {
                    Map<String, Object> requestData = (Map<String, Object>) arg;
                    Object cartItemsObj = requestData.get("cartItems");

                    if (cartItemsObj != null) {
                        try {
                            // 使用ObjectMapper将cartItems转为JSON字符串
                            return objectMapper.writeValueAsString(cartItemsObj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "未能解析购买商品信息";
    }
    @Around("@annotation(com.wangchen.annotations.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //操作人ID - 当前登录员工ID
        //获取请求头中的jwt令牌, 解析令牌
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        Integer operateUser = (Integer) claims.get("userid");

        //操作时间
        LocalDateTime operateTime = LocalDateTime.now();

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        // 解析购买商品信息
        String purchaseInfo = parsePurchaseInfo(args);

        long begin = System.currentTimeMillis();
        //调用原始目标方法运行
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        String operateType = null;
        String productName = null;
        Result result1 = (Result) result;
        if(result1.getData() instanceof Product){
            Product product = (Product)result1.getData();
            productName = product.getProductName();
        }
        //操作耗时
        Long costTime = end - begin;

        //操作方法名
        String methodName = joinPoint.getSignature().getName();
        String operate = null;

        if(methodName.equals("sendEmailAndBuy")){
            operate = new String("购买商品");
            operateType = new String("执行了 : "+operate+" 操作，购买商品信息：" + purchaseInfo +" 操作耗时 : "+costTime +"ms");
        }else if(methodName.equals("getProductByID")){
            operate = new String("浏览了商品");
            operateType = new String("执行了 : "+operate+" 操作，浏览商品名：" + productName +" 操作耗时 : "+costTime +"ms");
        }
        //记录操作日志
        CustomerLog customerLog= new CustomerLog(0,operateUser,operateType,operateTime);
        customerLogMapper.insertCustomerLog(customerLog);

        return result;
    }

}
