package com.kanfs.omas.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【role】的数据库操作Mapper
* @createDate 2023-12-12 15:38:16
* @Entity com.kanfs.pojo.omas.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectAll();

    List<Role> selectPageList(Page<Role> rolePage, Map searchObj);

    int getTotalRowCount();

    int insertRole(Map<String, Object> params);
}




