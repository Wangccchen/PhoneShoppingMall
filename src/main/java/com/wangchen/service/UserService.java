package com.wangchen.service;

import com.wangchen.pojo.PageBean;
import com.wangchen.pojo.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserByID(int userID);

    void addUser(User user);

    void deleteUser(int userID);

    void deleteUsersByIds(List<Integer> userIds);

    void updateUser(User user);

    public PageBean getUsersByPage(int currentPage, int pageSize);
    // 根据关键字搜索用户（模糊搜索）
    List<User> searchUsersByUsername(String keyword);

    // 根据用户名获取用户
    User getUserByUsername(String username);

    //后台验证登录
    User login(String username, String password);

    int getTotalUsersCount();
}

