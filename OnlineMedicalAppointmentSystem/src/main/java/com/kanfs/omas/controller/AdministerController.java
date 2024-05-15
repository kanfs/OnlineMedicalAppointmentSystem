package com.kanfs.omas.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Administer;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.AdministerService;
import com.kanfs.omas.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CrossOrigin()
@RestController
public class AdministerController {
    @Autowired
    AdministerService administerService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/mas/admin/login")
    public Result login(@RequestBody Map params){
        Map<String, Object> result = administerService.login(params);
        if( result.get("code").equals(200) ){
            String token = tokenService.getToken((Integer) result.get("adminId"), (String)params.get("password"));
            result.put("token", token);
        }
        return Result.ok(result);
    }
    @GetMapping("/mas/admin/info")
    public Result info(@RequestParam Map params) {
        // 获取请求头token字符串
        String token = (String) params.get("token");
        int id = tokenService.getIdByToken(token);
        Map<String, Object> result = administerService.getUserInfo(id);
        return Result.ok(result);
    }
    @PostMapping("/mas/admin/logout")
    public Result logout(){
        return Result.ok();
    }
//      getPageList(page, limit, searchObj) {
//    return request({
//      url: `${api_name}/${page}/${limit}`,
//      method: 'get',
//      params: searchObj // url查询字符串或表单键值对
//    })
    //@PreAuthorize("hasAuthority('bnt.admin.list')")
    @GetMapping("/mas/admin/system/admin/{page}/{limit}")
    public Result getPageList(@PathVariable int page, @PathVariable int limit, @RequestParam Map searchObj){
        return Result.ok(administerService.getPageList(new Page<Administer>(page, limit), searchObj));
    }

    //@PreAuthorize("hasAuthority('bnt.admin.info')")
    @GetMapping("/mas/admin/system/admin/get/{id}")
    public Result getAdminById(@PathVariable int id){
        return Result.ok(administerService.getAdminById(id));
    }

    //@PreAuthorize("hasAuthority('bnt.admin.insert')")
    @PostMapping("/mas/admin/system/admin/save")
    public Result insertAdmin(@RequestBody Map admin){
        Map<String, Object> res = administerService.insertAdmin(admin);
        if(res.get("code").equals("1")) return Result.build(res, 201, "该用户名已被使用");
        return Result.ok(res);
    }

    //@PreAuthorize("hasAuthority('bnt.admin.update')")
    @PostMapping("/mas/admin/system/admin/update")
    public Result updateAdmin(@RequestBody Map admin){
        Map<String, Object> res = administerService.updateAdmin(admin);
        return Result.ok(res);
    }

    //@PreAuthorize("hasAuthority('bnt.admin.status')")
    @GetMapping("/mas/admin/system/admin/updateStatus/{id}/{status}")
    public Result updateAdminStatus(@PathVariable int id, @PathVariable String status){
        boolean flag = status.equals("true");
        Map<String, Object> res = administerService.updateAdminStatus(id, flag);
        return Result.ok(res);
    }

    //@PreAuthorize("hasAuthority('bnt.admin.delete')")
    @DeleteMapping("/mas/admin/system/admin/remove/{id}")
    public  Result removeAdmin(@PathVariable int id){
        Map<String, Object> res = administerService.deleteAdminById(id);
        return Result.ok(res);
    }
}
