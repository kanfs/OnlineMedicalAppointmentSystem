package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.Vo.RouterVo;
import com.kanfs.omas.mapper.RoleMenuMapper;
import com.kanfs.omas.pojo.Menu;
import com.kanfs.omas.pojo.RoleMenu;
import com.kanfs.omas.service.MenuService;
import com.kanfs.omas.mapper.MenuMapper;
import com.kanfs.omas.utils.MenuUtil;
import com.kanfs.omas.utils.RouterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 方盛
* @description 针对表【menu(菜单表)】的数据库操作Service实现

*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> findNodes() {
        List<Menu> menus = this.list();
        if (CollectionUtils.isEmpty(menus)) {
            return null;
        }

        //构建树形数据
        List<Menu> result = MenuUtil.buildTree(menus);
        return result;
    }

    @Override
    public void removeMenuById(int id) {
        // 删除菜单的时候要将角色和菜单关联的关系删除
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getMenuId, id));

        baseMapper.deleteById(id);
    }

    @Override
    public List<Menu> findMenuByRoleId(String roleId) {
        //获取所有status为1的权限列表
        List<Menu> menuList = menuMapper.selectList(
                new LambdaQueryWrapper<Menu>()
                        .eq(Menu::getStatus, 1)
        );
        //根据角色id获取角色权限
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(
                new LambdaQueryWrapper<RoleMenu>()
                        .eq(RoleMenu::getRoleId, roleId)
        );
        //获取该角色已分配的所有权限id
        Set<Long> roleMenuIds = roleMenus.stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toSet());

        //遍历所有权限列表
        for (Menu menu : menuList) {
            if (roleMenuIds.contains(menu.getId())) {
                //设置该权限已被分配
                menu.setSelect(true);
            } else {
                menu.setSelect(false);
            }
        }
        //将权限列表转换为权限树
        List<Menu> menus = MenuUtil.buildTree(menuList);
        return menus;
    }

    @Override
    public void doAssign(Map<String, Object> params) {
        //删除已分配的权限
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, Long.parseLong((String) params.get("roleId"))));
        //遍历所有已选择的权限id
        ((List<Integer>)params.get("menuIdList")).forEach(menuId -> {
                    Optional.ofNullable(menuId).ifPresent(menuid -> {
                                //添加新权限
                                roleMenuMapper.insert(new RoleMenu((Long.parseLong((String) params.get("roleId"))), menuid));
                            });

                });

    }

    @Override
    public List<RouterVo> findAdminMenuList(int id) {
        List<Menu> menuList = menuMapper.findListByAdminId(id);
        //构建菜单树
        List<Menu> menuTreeList = MenuUtil.buildTree(menuList);

        //构建路由
        List<RouterVo> routerVoList = RouterUtil.buildRouters(menuTreeList);
        return routerVoList;
    }

    @Override
    public List<String> findAdminPermsList(int id) {
        List<Menu> menuList = menuMapper.findListByAdminId(id);

        //创建返回的集合
        return menuList.stream()
                .filter(s -> s.getType() == 2)
                .map(Menu::getPerms)
                .collect(Collectors.toList());
    }
}




