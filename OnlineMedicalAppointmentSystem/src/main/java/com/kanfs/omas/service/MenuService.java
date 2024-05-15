package com.kanfs.omas.service;

import com.kanfs.omas.Vo.RouterVo;
import com.kanfs.omas.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【menu(菜单表)】的数据库操作Service

*/
public interface MenuService extends IService<Menu> {

    List<RouterVo> findAdminMenuList(int id);

    List<String> findAdminPermsList(int id);

    List<Menu> findNodes();

    void removeMenuById(int id);

    List<Menu> findMenuByRoleId(String roleId);

    void doAssign(Map<String, Object> assignMenuVo);
}
