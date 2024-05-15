package com.kanfs.omas.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Administer;
import com.kanfs.omas.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【role】的数据库操作Service
* @createDate 2023-12-12 15:38:16
*/
public interface RoleService extends IService<Role> {

    Map<String, Object> getRolesByAdminId(int adminId);

    Map<String, Object> doAssign(Map<String, Object> params);

    Map<String, Object> getPageList(Page<Role> rolePage, Map searchObj);

    Map<String, Object> saveRole(Map<String, Object> params);

    void removeRoleByIds(List<Integer> idList);
}
