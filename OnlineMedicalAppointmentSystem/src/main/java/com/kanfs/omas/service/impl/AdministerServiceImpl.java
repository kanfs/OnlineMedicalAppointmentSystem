package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.Vo.DepartmentVo;
import com.kanfs.omas.Vo.RouterVo;
import com.kanfs.omas.mapper.*;
import com.kanfs.omas.pojo.AdminRole;
import com.kanfs.omas.pojo.Administer;
import com.kanfs.omas.pojo.Doctor;
import com.kanfs.omas.pojo.Role;
import com.kanfs.omas.service.AdminHospitalService;
import com.kanfs.omas.service.AdministerService;
import com.kanfs.omas.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
* @author 方盛
* @description 针对表【administer】的数据库操作Service实现
* @createDate 2023-12-12 15:38:16
*/
@Service
public class AdministerServiceImpl extends ServiceImpl<AdministerMapper, Administer>
    implements AdministerService{
    @Autowired
    private AdministerMapper administerMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private AdminHospitalMapper adminHospitalMapper;
    @Autowired
    private AdminDoctorMapper adminDoctorMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Map<String, Object> login(Map params) {
        Administer administer = administerMapper.getAdminByUserName((String)params.get("username"));
        Map<String, Object> result = new HashMap<String, Object>();
        if (administer == null){
            result.put("code", 1);
            result.put("msg", "用户不存在");
        }else{
            if (administer.getPassword().equals(params.get("password"))){
                result.put("code", 200);
                result.put("msg", "登录成功");
                result.put("adminId", administer.getId());
            }else{
                result.put("code", 1);
                result.put("msg", "密码错误");
            }
        }
        return result;
    }

//    @Override
//    public Map<String, Object> getAdminById(int id) {
//        Administer administer = administerMapper.selectAdminById(id);
//        Map<String, Object> result = new HashMap<String, Object>();
//        if (administer != null) {
//            result.put("code", 200);
//            result.put("msg", "获取成功");
//            result.put("adminId", administer.getId());
//            result.put("username", administer.getName());
//        }else{
//            result.put("code", 1);
//            result.put("msg", "获取失败");
//        }
//        return result;
//    }

    @Override
    public Map<String, Object> getUserInfo(int id){
        Map<String, Object> result = new HashMap<String, Object>();
        Administer administer = administerMapper.selectAdminById(id);
        //根据用户id获取对应医院id
        Long hospitalId = adminHospitalMapper.findHospitalIdByAdminId(id);
        //根据用户id获取对应医生id
        Long doctorId = adminDoctorMapper.findDoctorIdByAdminId(id);
        if( doctorId!=null ) hospitalId = doctorMapper.getDoctorInfo(doctorId).getHospitalId();
        //根据用户id获取菜单权限值
        List<RouterVo> routerVoList = menuService.findAdminMenuList(id);
        //根据用户id获取用户按钮权限
        List<String> permsList = menuService.findAdminPermsList(id);
        result.put("name", administer.getName());
        result.put("hospitalId", hospitalId);
        result.put("doctorId", doctorId);
        result.put("avatar", administer.getHeadUrl());
        //当前权限控制使用不到，我们暂时忽略
        result.put("roles", new HashSet<>());
        result.put("buttons", permsList);
        result.put("routers", routerVoList);
        return result;
    }

    @Override
    public Map<String, Object> getPageList(Page<Administer> administerPage, Map searchObj) {
        Map<String, Object> res = new HashMap<>();
        List<Administer> administers = administerMapper.selectPageList(administerPage, searchObj);
        List<Map<String, Object>> records = new ArrayList<>();
        for (Administer administer : administers){
            Map<String, Object> record = new HashMap<>();
            record.put("id", administer.getId());
            record.put("name", administer.getName());
            record.put("status", administer.isStatus());
            record.put("username", administer.getUsername());
            record.put("headUrl", administer.getHeadUrl());
            record.put("phone", administer.getPhone());
            record.put("createTime", administer.getCreateTime());
            List<AdminRole> adminRoles = adminRoleMapper.selectListById(administer.getId());
            if(adminRoles.size()>0)
            {
                int roleId = adminRoles.get(0).getRoleId();
                String role = roleMapper.selectOne(new QueryWrapper<Role>().eq("id", roleId)).getName();
                if ((role != null && role.equals(searchObj.get("role"))) || ((searchObj.get("role") == null) || (searchObj.get("role").equals("")))) {
                    record.put("role", role);
                    records.add(record);
                }
            }
            else {
                record.put("role", "暂无");
                records.add(record);
            }
        }
        res.put("records", records);
        res.put("total", records.size());
        return res;
    }

    @Override
    public Administer getAdminById(int id) {
        return administerMapper.selectAdminById(id);
    }

    @Override
    public Map<String, Object> insertAdmin(Map params) {
        String username = (String) params.get("username");
        Map<String, Object> res = new HashMap<>();
        if( administerMapper.selectOne(new QueryWrapper<Administer>().eq("username", username)) != null){
            res.put("code", 1);
            res.put("msg","该用户名已被使用");
        }else{
            params.put("createTime", new Date());
            params.put("status", true);
            params.put("isDeleted", false);
            if (administerMapper.insertAdmin(params)>0){
                res.put("code", 200);
                res.put("msg","更新成功");
            }
        }
        return res;
    }

    @Override
    public Map<String, Object> updateAdmin(Map params) {
        Map<String, Object> res = new HashMap<>();
        res.put("affectedRows", administerMapper.updateAdmin(params));
        res.put("code", 200);
        res.put("msg","更新成功");
        return res;
    }

    @Override
    public Map<String, Object> updateAdminStatus(int id, boolean status) {
        Map<String, Object> res = new HashMap<>();
        if (administerMapper.updateAdminStatus(id, status)>0){
            res.put("code", 200);
            res.put("msg","更新状态成功");
        }
        return res;
    }

    @Override
    public Map<String, Object> deleteAdminById(int id) {
        Administer administer = administerMapper.selectAdminById(id);
        Map<String, Object> res = new HashMap<>();
        if( administer == null){
            res.put("code", 1);
            res.put("msg", "该用户不存在");
        }else{
            if (administerMapper.deleteAdminById(id)>0){
                res.put("code", 200);
                res.put("msg","删除成功");
            }
        }
        return res;
    }
}




