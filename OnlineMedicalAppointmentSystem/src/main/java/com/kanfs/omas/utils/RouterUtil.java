package com.kanfs.omas.utils;

import com.kanfs.omas.Vo.MetaVo;
import com.kanfs.omas.Vo.RouterVo;
import com.kanfs.omas.pojo.Menu;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RouterUtil {
    /**
     * 根据菜单构建路由
     *
     * @param menus
     * @return
     */
    public static List<RouterVo> buildRouters(List<Menu> menus) {
        //创建路由列表
        List<RouterVo> routers = new LinkedList<RouterVo>();

        //遍历菜单列表
        menus.forEach(menu -> {
            //创建路由
            RouterVo router = RouterVo.builder()
                    .hidden(false)
                    .alwaysShow(false)
                    .path(getRouterPath(menu))
                    .component(menu.getComponent())
                    .meta(new MetaVo(menu.getName(), menu.getIcon()))
                    .build();
            //获取子菜单
            List<Menu> children = menu.getChildren();
            //如果当前是菜单，需将按钮对应的路由加载出来，如：“角色授权”按钮对应的路由在“系统管理”下面
            if (menu.getType().intValue() == 1) {
                //过滤出按钮类型的子菜单
                List<Menu> hiddenMenuList = children.stream()
                        .filter(item -> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                //遍历按钮类型的子菜单
                hiddenMenuList.forEach(hiddenMenu ->
                        routers.add(
                                RouterVo.builder()
                                        .hidden(true)
                                        .alwaysShow(false)
                                        .path(getRouterPath(hiddenMenu))
                                        .component(hiddenMenu.getComponent())
                                        .meta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()))
                                        .build()
                        )
                );

            } else {
                //如果子菜单不为空
                if (!CollectionUtils.isEmpty(children)) {
                    //如果子菜单数量大于0
                    if (children.size() > 0) {
                        //设置路由始终显示
                        router.setAlwaysShow(true);
                    }
                    //递归调用，获取子菜单的路由
                    router.setChildren(buildRouters(children));
                }
            }

            //将路由添加到路由列表
            routers.add(router);
        });
        //返回路由列表
        return routers;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public static String getRouterPath(Menu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }
}
