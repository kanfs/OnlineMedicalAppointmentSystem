package com.kanfs.omas.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【user】的数据库操作Service
* @createDate 2023-12-12 15:38:16
*/
public interface UserService extends IService<User> {
    // 根据条件查询用户总数
    Integer count(Map<String, Object> params);
    // 分页获取用户信息
    List<User> getUsers(Map<String, Object> params);
    // 根据id获取客户信息
    Map<String, Object> getUserInfoById(int id);
    // 根据电话号码获取用户信息
    User getUserByEmail(String email);
    // 根据姓名获取用户信息
    User getUserByName(String name);
    // 修改用户信息
    Map<String, Object> updateUserInfo(Map<String, Object> map);
    // 用户注册
    Map<String, Object> registerUser(Map map);

    void sendCode(String email);

    Map<String, Object> selectPage(Page<User> userPage, Map<String, Object> searchObj);

    Map<String, Object> selectPageWithPatients(Page<User> userPage, Map<String, Object> searchObj);
}
