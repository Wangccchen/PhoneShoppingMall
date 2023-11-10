package com.wangchen.service.impl;

import com.wangchen.mapper.UserMapper;
import com.wangchen.pojo.PageBean;
import com.wangchen.pojo.User;
import com.wangchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public User getUserByID(int userID) {
        return userMapper.getUserById(userID);
    }

    @Override
    public void addUser(User user) {
        userMapper.addUser(user);
    }

    @Override
    public void deleteUser(int userID) {
        userMapper.deleteUser(userID);
    }

    @Override
    public void deleteUsersByIds(List<Integer> userIds) {
        userMapper.deleteUsersByIds(userIds);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    // 根据关键字搜索用户（模糊搜索）
    @Override
    public List<User> searchUsersByUsername(String keyword) {
        return userMapper.searchUsersByUsername(keyword);
    }

    // 根据用户名获取用户
    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public PageBean getUsersByPage(int currentPage, int pageSize) {
        List<User> users = userMapper.getUsersByPage((currentPage - 1) * pageSize, pageSize);
        int totalItems = userMapper.getTotalUsersCount();  // 获取总用户数量
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);  // 计算总页数

        PageBean pageBean = new PageBean();
        pageBean.setPageNum(currentPage);
        pageBean.setPageSize(pageSize);
        pageBean.setTotalItems(totalItems);
        pageBean.setTotalPages(totalPages);
        pageBean.setUsers(users);

        return pageBean;
    }


}

