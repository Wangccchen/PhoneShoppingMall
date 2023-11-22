package com.wangchen.mapper;

import com.wangchen.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    // 增加用户
    void addUser(User user);

    // 删除用户
    void deleteUser(int userID);

    // 批量删除用户
    void deleteUsersByIds(List<Integer> userIds);
    // 更新用户信息
    void updateUser(User user);

    // 根据ID获取用户
    User getUserById(int userID);

    // 根据用户名获取用户
    User getUserByUsername(String username);

    // 获取所有用户
    List<User> getAllUsers();

    // 根据关键字搜索用户（模糊搜索）
    List<User> searchUsersByUsername(@Param("keyword") String keyword);

    // 分页查询用户
    List<User> getUsersByPage(@Param("start") int start, @Param("pageSize") int pageSize);

    // 获取用户总数
    int getTotalUsersCount();

    //后台验证登录
    User getUserByUsernameAndPassword(String username, String password);
}

