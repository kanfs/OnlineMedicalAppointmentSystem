package com.kanfs.omas.utils;

import com.kanfs.omas.pojo.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuUtil {
    /**
     * 使用递归方法建菜单
     * @param menuList
     * @return
     */
    public static List<Menu> buildTree(List<Menu> menuList) {
        return menuList.stream()
                .filter(s -> s.getParentId().longValue() == 0)
                .map(s -> findChildren(s, menuList))
                .collect(Collectors.toList());
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    public static Menu findChildren(Menu menu, List<Menu> treeNodes) {
        menu.setChildren(new ArrayList<Menu>());
        treeNodes.stream()
                .filter(it -> menu.getId().longValue() == it.getParentId().longValue())
                .forEach(it -> {
                    if (menu.getChildren() == null) {
                        menu.setChildren(new ArrayList<>());
                    }
                    menu.getChildren().add(findChildren(it, treeNodes));
                });
        return menu;
    }
}
