package com.kanfs.omas.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-12-12 15:38:16
* @Entity com.kanfs.pojo.omas.User
*/
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 根据用户id获取用户信息
    User findUserById(int id);
    // 根据条件查询用户总数
    Integer count(Map<String, Object> params);
    // 分页获取用户信息
    List<User> findByPage(Map<String, Object> params);
    // 根据电话号码获取用户信息
    User findUserByEmail(String email);
    // 根据用户姓名获取用户信息
    User findUserByName(String name);
    // 注册用户
    int insertUser(User user);
    // 更新用户注册信息
    int updateUserInfo(Map<String, Object> params);

    List<User> selectPageList(Page<User> userPage, Map<String, Object> searchObj);

    int getTotalRowCount();
}




