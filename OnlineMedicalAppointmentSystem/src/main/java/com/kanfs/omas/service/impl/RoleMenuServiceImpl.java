package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.pojo.RoleMenu;
import com.kanfs.omas.service.RoleMenuService;
import com.kanfs.omas.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 方盛
* @description 针对表【role_menu(角色菜单)】的数据库操作Service实现

*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService {
    @Autowired
    private RoleMenuMapper roleMenuMapper;
}




