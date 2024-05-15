package com.kanfs.omas.controller;

import com.kanfs.omas.pojo.Menu;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/mas/admin/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    //@PreAuthorize("hasAuthority('bnt.menu.list')")
    @GetMapping("/findNodes")
    public Result findNodes() {
        List<Menu> menuList = menuService.findNodes();
        return Result.ok(menuList);
    }

    //@PreAuthorize("hasAuthority('bnt.menu.remove')")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable int id) {
        menuService.removeMenuById(id);
        return Result.ok();
    }


    //@PreAuthorize("hasAuthority('bnt.menu.add')")
    @PostMapping("/save")
    public Result save(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.ok();
    }


    //@PreAuthorize("hasAuthority('bnt.menu.update')")
    @PostMapping("/update")
    public Result get(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Result.ok(menu);
    }

    //@PreAuthorize("hasAuthority('bnt.role.assignAuth')")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable String roleId) {
        List<Menu> list = menuService.findMenuByRoleId(roleId);
        return Result.ok(list);
    }

    //@PreAuthorize("hasAuthority('bnt.role.assignAuth')")
    @PostMapping("/doAssign")
    public Result toAssign(@RequestBody Map<String, Object> assignMenuVo) {
        menuService.doAssign(assignMenuVo);
        return Result.ok();
    }
}
