package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.Vo.DepartmentVo;
import com.kanfs.omas.mapper.DoctorMapper;
import com.kanfs.omas.mapper.HospitalMapper;
import com.kanfs.omas.pojo.Department;
import com.kanfs.omas.pojo.Doctor;
import com.kanfs.omas.pojo.Hospital;
import com.kanfs.omas.service.DepartmentService;
import com.kanfs.omas.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【department】的数据库操作Service实现
* @createDate 2023-12-12 15:38:16
*/
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
    implements DepartmentService{
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    HospitalMapper hospitalMapper;
    @Autowired
    DoctorMapper doctorMapper;

    @Override
    public List<String> getDepartmentNames(String hospitalName) {
        return departmentMapper.findDepartmentNames(hospitalName);
    }

    @Override
    public Map<String, Object> getNames(long departmentId) {
        Department department = departmentMapper.selectById(departmentId);
        Map<String ,Object> res = new HashMap<>();
        res.put("departmentName", department.getName());
        res.put("hospitalId", department.getHospitalId());
        res.put("hospitalName", hospitalMapper.selectById(department.getHospitalId()).getName());
        return res;
    }

    @Override
    public Map<String, Object> selectPage(Page<Department> departmentPage, Map<String, Object> searchObj) {
        Map<String, Object> res = new HashMap<>();
        List<DepartmentVo> departments = departmentMapper.selectPageList(departmentPage, searchObj);
        List<Map<String, Object>> records = new ArrayList<>();
        for (DepartmentVo department : departments){

            Map<String, Object> record = new HashMap<>();
            record.put("id", department.getId());
            record.put("name", department.getName());
            record.put("hospitalName", department.getHospitalName());
            record.put("doctorNum", department.getDoctorNum());
            record.put("intro", department.getIntro());
            record.put("doctorList", doctorMapper.selectList(new QueryWrapper<Doctor>().eq("departmentId", department.getId())));
            records.add(record);
        }
        res.put("records", records);
        res.put("total", departmentMapper.getTotalRowCount());
        return res;
    }
}





