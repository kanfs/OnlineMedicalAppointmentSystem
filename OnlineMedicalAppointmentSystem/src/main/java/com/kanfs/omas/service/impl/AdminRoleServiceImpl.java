package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.pojo.AdminRole;
import com.kanfs.omas.service.AdminRoleService;
import com.kanfs.omas.mapper.AdminRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 方盛
* @description 针对表【admin_role】的数据库操作Service实现
*/
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole>
    implements AdminRoleService{

}




