package com.kanfs.omas.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Administer;
import com.kanfs.omas.pojo.Role;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("/mas/admin/system/role")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    //@PreAuthorize("hasAuthority('bnt.role.list')")
    @GetMapping("/{page}/{limit}")
    public Result getPageList(@PathVariable int page, @PathVariable int limit, @RequestParam Map searchObj){
        return Result.ok(roleService.getPageList(new Page<Role>(page, limit), searchObj));
    }

    //@PreAuthorize("hasAuthority('bnt.role.add')")
    @PostMapping("/save")
    public Result save(@RequestBody Map<String, Object> role){
        return Result.ok(roleService.saveRole(role));
    }

    //@PreAuthorize("hasAuthority('bnt.role.remove')")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable int id){
        return Result.ok(roleService.removeById(id));
    }

    //@PreAuthorize("hasAuthority('bnt.role.update')")
    @PostMapping("/update")
    public Result update(@RequestBody Role role){
        return Result.ok(roleService.save(role));
    }

    //@PreAuthorize("hasAuthority('bnt.role.list')")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable int id){
        return Result.ok(roleService.getById(id));
    }

    //@PreAuthorize("hasAuthority('bnt.role.remove')")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Integer> idList) {
        roleService.removeRoleByIds(idList);
        return Result.ok();
    }

    //@PreAuthorize("hasAuthority('bnt.admin.assignRole')")
    @GetMapping("/toAssign/{adminId}")
    public Result toAssign(@PathVariable int adminId){
        Map<String, Object> roleMap = roleService.getRolesByAdminId(adminId);
        return Result.ok(roleMap);
    }

    //@PreAuthorize("hasAuthority('bnt.admin.assignRole')")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody Map<String, Object> params){
        Map<String, Object> res = roleService.doAssign(params);
        return Result.ok(res);
    }
}
