package com.wangchen.controller;

import com.wangchen.pojo.PageBean;
import com.wangchen.pojo.Result;
import com.wangchen.pojo.User;
import com.wangchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // 获取所有用户信息
    @GetMapping({"/users","/backsystem/users"})
    public Result getUsersByPage(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageBean pageBean = userService.getUsersByPage(currentPage, pageSize);
        return Result.success(pageBean);
    }

    // 获取所有用户信息
    @GetMapping({"/users/allUser","/backsystem/users/allUser"})
    public Result getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }

    // 根据用户ID获取用户详情
    @GetMapping({"/users/{userID}","/backsystem/users/{userID}"})
    public Result getUserByID(@PathVariable("userID") int userID) {
        User user = userService.getUserByID(userID);
        return Result.success(user);
    }

    // 根据关键字搜索用户（模糊搜索）
    @GetMapping({"/users/search","/backsystem/users/search"})
    public Result searchUsers(@RequestParam String keyword) {
        List<User> users = userService.searchUsersByUsername(keyword);
        return Result.success(users);
    }

    // 根据用户名获取用户
    @GetMapping({"/users/getByUsername","/backsystem/users/getByUsername"})
    public Result getUserByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        return Result.success(user);
    }

    // 添加用户
    @PostMapping({"/users","/backsystem/users"})
    public Result addUser(@RequestBody User user) {
        userService.addUser(user);
        return Result.success("User added successfully");
    }

    // 删除用户
    @DeleteMapping({"/users/delete/{userID}","/backsystem/users/delete/{userID}"})
    public Result deleteUser(@PathVariable("userID") int userID) {
        userService.deleteUser(userID);
        return Result.success("User deleted successfully");
    }

    // 批量删除用户
    @PostMapping({"/users/deleteBatch","/backsystem/users/deleteBatch"})
    public Result deleteUsersBatch(@RequestBody List<Integer> userIds) {
        try {
            userService.deleteUsersByIds(userIds);
            return Result.success("批量删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }

    // 更新用户信息
    @PutMapping({"/users","/backsystem/users"})
    public Result updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success("User updated successfully");
    }

    @GetMapping("/backsystem/users/getTotal")
    public Result getTotalUsersCount(){
        try {
            int total = userService.getTotalUsersCount();
            return Result.success(total);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取用户数量失败：" + e.getMessage());
        }

    }
}

