
package com.wangchen.controller;

import com.wangchen.pojo.Result;
import com.wangchen.pojo.User;
import com.wangchen.service.UserService;
import com.wangchen.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping({"/back/login","/login"})
    public Result login(HttpServletRequest request, @RequestBody User user) {
        try {
            User loginUser = userService.login(user.getUsername(), user.getPassword());

            // 获取请求的URL
            String requestUrl = request.getRequestURI();

            if ("/back/login".equals(requestUrl)) {
                // 当请求的URL是/back/login时，检查flag字段
                if(!loginUser.getFlag().equals("1")){
                    //说明不是管理员
                    //直接返回错误
                    return Result.error("您不是管理员，无法登录后台!");
                }
                if (loginUser != null ) {
                    Map<String , Object> claims = new HashMap<>();
                    claims.put("userid", loginUser.getUserID());
                    claims.put("username",loginUser.getUsername());
                    claims.put("email",loginUser.getEmail());
                    // 使用JWT工具类，生成身份令牌
                    String token = JwtUtils.generateJwt(claims);
                    return Result.success(token);
                } else {
                    return Result.error("管理员登录失败!用户名或密码错误!");
                }
            } else {
                // 其他情况，按照原有逻辑处理
                if (loginUser != null) {
                    Map<String , Object> claims = new HashMap<>();
                    claims.put("userid", loginUser.getUserID());
                    claims.put("username",loginUser.getUsername());
                    claims.put("email",loginUser.getEmail());
                    // 使用JWT工具类，生成身份令牌
                    String token = JwtUtils.generateJwt(claims);
                    return Result.success(token);
                } else {
                    return Result.error("登录失败!用户名或密码错误!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("请完整输入用户名和密码!");
        }
    }
}

