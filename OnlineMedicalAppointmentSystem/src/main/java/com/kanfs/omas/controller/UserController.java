package com.kanfs.omas.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.annotation.UserLoginToken;
import com.kanfs.omas.pojo.User;
import com.kanfs.omas.pojo.Pager;
import com.kanfs.omas.pojo.User;
import com.kanfs.omas.result.Result;
import com.kanfs.omas.service.TokenService;
import com.kanfs.omas.service.UserService;
import com.kanfs.omas.utils.DozerUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private DozerUtil dozerUtil;

    /*@RequestParam String name,@RequestParam String password, @RequestParam String phoneNumber,
                                        @RequestParam String province, @RequestParam String city, @RequestParam String sex,
                                        @RequestParam String email*/
    // 接收注册申请
    @PostMapping("/mas/register")
    public @ResponseBody Map<String, Object> register(@RequestBody Map map){
        Map<String, Object> result = userService.registerUser(map);
        return result;
    }

    // 用户登录 @RequestParam String password, @RequestParam String email
    @PostMapping("/mas/login")
    public @ResponseBody Map<String, Object> login(@RequestBody Map map){
        String email = (String) map.get("emailAddress");
        String password = (String) map.get("password");
        Map<String, Object> result = new HashMap<>();
        User user = userService.getUserByEmail(email);
        if ( user!=null )
        {
            if( user.getPassword().equals(password) )
            {
                String token = tokenService.getToken(user.getId(), user.getPassword());
                result.put("code", 0);
                result.put("msg", "登陆成功");
                result.put("token", token);
                result.put("userId", user.getId());
                result.put("name", user.getName());
            }else{
                result.put("code", 1);
                result.put("msg", "登陆失败，密码错误");
            }
        }else{
            result.put("code", 2);
            result.put("msg", "登陆失败，该邮箱尚未注册");
        }
        return result;
    }

    // 分页获取用户列表
    @GetMapping("/mas/customers")
    public Map<String, Object> getCustomers(Integer curPage, Integer pageSize, User user){
        // map对象result保存查询结果
        Map<String, Object> result = new HashMap<String, Object>();
        // map对象params封装查询条件
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", user.getName());
        int userCount = userService.count(params);

        // 创建分页类对象
        Pager pager = new Pager();
        pager.setCurPage(curPage);
        pager.setPerPageRows(pageSize);
        params.put("pager", pager);

        // 根据查询条件分页获取用户列表
        List<User> userList = userService.getUsers(params);
        if ( userList.size() > 0 )
        {
            result.put("code", 0);
            result.put("page", curPage);
            result.put("count", userCount);
            result.put("userList", userList);
            result.put("msg", "获取用户列表成功");
        }else{
            result.put("code", 1);
            result.put("msg", "没有用户记录");
        }
        return result;
    }

    // 根据用户id获取用户信息
    @GetMapping("/mas/user/info/{id}")
    public @ResponseBody Map<String, Object> getUserInfoById(@PathVariable int id){
        // map对象result保存查询结果
        Map<String, Object> result = userService.getUserInfoById(id);
        return result;
    }

    // 判断用户名称和id是否存在
    @GetMapping("/mas/user/name/{name}/id/{id}")
    public Map<String, Object> isExistUserName_Id(@PathVariable String name, @PathVariable int id) {
        Map<String, Object> result = new HashMap<String, Object>();
        User user = userService.getUserByName(name);
        if( user != null && user.getId() != id)
            result.put("code", 0);
        else{
            result.put("code", 1);
            result.put("msg", String.format("该id:%s已被注册", id));
        }
        return result;
    }

    // 修改用户信息
    @UserLoginToken
    @GetMapping("/mas/user/update")
    public Map<String, Object> updateUserInfo(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = userService.updateUserInfo(params);
        return result;
    }

    @PostMapping("/mas/testCode")
    public Map<String, Object> getCode(@RequestBody Map map) {
        String email = (String) map.get("email");
        Map<String, Object> result = new HashMap<String, Object>();
        // 数据库查询邮箱号是否已注册
        User user = userService.getUserByEmail(email);
        if(user!=null)
        {
            result.put("code", 1);
            result.put("msg", String.format("该邮箱:%s已被注册",email));
        }else {
            userService.sendCode(email);
        }
        return result;
    }

    //@PreAuthorize("hasAuthority('bnt.user.list')")
    @GetMapping("/mas/admin/user/user/{page}/{limit}")
    public Result getPageList(@PathVariable int page, @PathVariable int limit, @RequestParam Map<String, Object> searchObj){
        return Result.ok(userService.selectPage(new Page<User>(page, limit), searchObj));
    }

    //@PreAuthorize("hasAuthority('bnt.user.list')")
    @GetMapping("/mas/admin/user/user/patient/{page}/{limit}")
    public Result getPageListWithPatients(@PathVariable int page, @PathVariable int limit, @RequestParam Map<String, Object> searchObj){
        return Result.ok(userService.selectPageWithPatients(new Page<User>(page, limit), searchObj));
    }

    //@PreAuthorize("hasAuthority('bnt.user.list')")
    @GetMapping("/mas/admin/user/user/get/{id}")
    public Result getById(@PathVariable long id){
        return Result.ok(userService.getById(id));
    }

    //@PreAuthorize("hasAuthority('bnt.user.add')")
    @PostMapping("/mas/admin/user/user/save")
    public Result save(@RequestBody Map<String, Object> params){
        User user = dozerUtil.map(params, User.class);
        user.setRegDate(new Date());
        return Result.ok(userService.save(user));
    }

    //@PreAuthorize("hasAuthority('bnt.user.update')")
    @PostMapping("/mas/admin/user/user/update")
    public Result update(@RequestBody Map<String, Object> params){
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse((String) params.remove("regDate"));
            params.put("regDate", date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        User user = dozerUtil.map(params, User.class);
        return Result.ok(userService.updateById(user));
    }

    //@PreAuthorize("hasAuthority('bnt.user.remove')")
    @DeleteMapping("/mas/admin/user/user/remove/{id}")
    public Result remove(@PathVariable long id){
        return Result.ok(userService.removeById(id));
    }
}
