package com.wangchen.controller;

import com.wangchen.pojo.PageBean;
import com.wangchen.pojo.Result;
import com.wangchen.pojo.User;
import com.wangchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // 获取所有用户信息
    @GetMapping
    public Result getUsersByPage(
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageBean pageBean = userService.getUsersByPage(currentPage, pageSize);
        return Result.success(pageBean);
    }

    // 获取所有用户信息
    @GetMapping("/allUser")
    public Result getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }

    // 根据用户ID获取用户详情
    @GetMapping("/{userID}")
    public Result getUserByID(@PathVariable("userID") int userID) {
        User user = userService.getUserByID(userID);
        return Result.success(user);
    }

    // 根据关键字搜索用户（模糊搜索）
    @GetMapping("/search")
    public Result searchUsers(@RequestParam String keyword) {
        List<User> users = userService.searchUsersByUsername(keyword);
        return Result.success(users);
    }

    // 根据用户名获取用户
    @GetMapping("/getByUsername")
    public Result getUserByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        return Result.success(user);
    }

    // 添加用户
    @PostMapping
    public Result addUser(@RequestBody User user) {
        userService.addUser(user);
        return Result.success("User added successfully");
    }

    // 删除用户
    @DeleteMapping("/delete/{userID}")
    public Result deleteUser(@PathVariable("userID") int userID) {
        userService.deleteUser(userID);
        return Result.success("User deleted successfully");
    }

    // 批量删除用户
    @PostMapping("/deleteBatch")
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
    @PutMapping
    public Result updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success("User updated successfully");
    }
}

