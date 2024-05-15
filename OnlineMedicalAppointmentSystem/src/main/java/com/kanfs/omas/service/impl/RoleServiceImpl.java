package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.mapper.AdminRoleMapper;
import com.kanfs.omas.mapper.RoleMenuMapper;
import com.kanfs.omas.pojo.*;
import com.kanfs.omas.service.AdminDoctorService;
import com.kanfs.omas.service.AdminHospitalService;
import com.kanfs.omas.service.RoleService;
import com.kanfs.omas.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author 方盛
* @description 针对表【role】的数据库操作Service实现
* @createDate 2023-12-12 15:38:16
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private AdminDoctorService adminDoctorService;
    @Autowired
    private AdminHospitalService adminHospitalService;

    @Override
    public Map<String, Object> getPageList(Page<Role> rolePage, Map searchObj) {
        Map<String, Object> res = new HashMap<>();
        res.put("records", roleMapper.selectPageList(rolePage, searchObj));
        res.put("total", roleMapper.getTotalRowCount());
        return res;
    }

    @Override
    public Map<String, Object> saveRole(Map<String, Object> params) {
        Map<String, Object> res = new HashMap<>();
        if (roleMapper.insertRole(params)>0){
            res.put("code", 200);
            res.put("msg","添加成功");
        }
        return res;
    }

    @Override
    public void removeRoleByIds(List<Integer> idList) {
        removeAdminAndRoleInfo(idList);
        baseMapper.deleteBatchIds(idList);
    }

    private void removeAdminAndRoleInfo(List<Integer> idList) {
        // 删除角色要将用户关联的角色信息删除
        adminRoleMapper.delete(
                new LambdaQueryWrapper<AdminRole>()
                        .in(AdminRole::getRoleId, idList)
        );
        // 删除角色关联的菜单信息
        roleMenuMapper.delete(
                new LambdaQueryWrapper<RoleMenu>()
                        .in(RoleMenu::getRoleId, idList)
        );
    }

    @Override
    public Map<String, Object> getRolesByAdminId(int adminId) {
        Map<String, Object> res = new HashMap<>();
        List<Role> roleList = roleMapper.selectAll();
        List<Role> roles = roleList
                .stream()
                .collect(Collectors.toList());
        List<AdminRole> adminRoles = adminRoleMapper.selectListById(adminId);
        List<Integer> userRoleIds = adminRoles.stream()
                .map(AdminRole::getRoleId)
                .collect(Collectors.toList());
        res.put("allRoles", roles);
        res.put("userRoleIds", userRoleIds);
        return res;
    }

    @Override
    public Map<String, Object> doAssign(Map<String, Object> params) {
//        System.out.println(params);
        adminRoleMapper.deleteByAdminId((Integer) params.get("adminId"));
        List<Integer> roleIdList = (List<Integer>) params.get("roleIdList");
        roleIdList.stream()
                .filter(roleId -> Objects.nonNull(roleId))
                .forEach(roleId -> {
                            //保存
                            adminRoleMapper.insertRole((Integer)params.get("adminId"), roleId);
                        }

                );
        if(roleIdList.size()==1 && roleIdList.get(0)==2 )
            adminHospitalService.save(
                    new AdminHospital().builder().
                            adminId((Integer)params.get("adminId") )
                            .hospitalId(Long.valueOf(params.get("hospitalId").toString()))
                            .build());
        if(roleIdList.size()==1 && roleIdList.get(0)==3)
            adminDoctorService.save(
                    new AdminDoctor().builder().
                            adminId((Integer)params.get("adminId"))
                            .doctorId((long) Integer.parseInt((String) params.get("doctorId")))
                            .build());
        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "设置成功");
        return res;
    }
}




