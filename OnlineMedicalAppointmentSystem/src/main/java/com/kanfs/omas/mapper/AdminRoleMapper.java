package com.kanfs.omas.mapper;

import com.kanfs.omas.pojo.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 方盛
* @description 针对表【admin_role】的数据库操作Mapper
* @Entity com.kanfs.omas.pojo.AdminRole
*/
@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

     int deleteByAdminId(int adminId) ;

     List<AdminRole> selectListById(int adminId);

    void insertRole(int adminId, int roleId);
}




