package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.Vo.DoctorVo;
import com.kanfs.omas.mapper.DepartmentMapper;
import com.kanfs.omas.mapper.DoctorMapper;
import com.kanfs.omas.mapper.HospitalMapper;
import com.kanfs.omas.pojo.Department;
import com.kanfs.omas.pojo.Doctor;
import com.kanfs.omas.pojo.Hospital;
import com.kanfs.omas.service.DoctorService;
import com.kanfs.omas.utils.DozerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【doctor】的数据库操作Service实现
* @createDate 2023-12-12 15:38:16
*/
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor>
    implements DoctorService {
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private HospitalMapper hospitalMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private DozerUtil dozerUtil;

    @Override
    public Map<String, Object> getMatchedDoctors(Map<String, Object> params) {
        Map<String, Object> res = doctorMapper.findMatchedDoctors(params);
        res.put("count", doctorMapper.getTotalRowCount());
        return res;
    }

    @Override
    public Map<String, Object> getDoctorInfo(long id) {
        Map<String, Object> res = new HashMap<String, Object>();
        Doctor doctor = doctorMapper.findDoctorById(id);
        if ( doctor != null){
            res.put("doctor", doctor);
            res.put("code", 200);
            res.put("msg", "success");
        }
        else {
            res.put("code", 1);
            res.put("msg", "doctor not found");
        }
        return res;
    }

    @Override
    public Map<String, Object> selectPage(Page<Doctor> doctorPage, Map<String, Object> searchObj) {
        Map<String, Object> res = doctorMapper.selectPageList(doctorPage, searchObj);
        res.put("count", doctorMapper.getTotalRowCount());
        return res;
    }

    @Override
    public DoctorVo getDoctorById(long id) {
        return doctorMapper.getDoctorInfo(id);
    }

    @Override
    public Map<String, Object> saveDoctor(Map<String, Object> params) {
        Hospital hospital = hospitalMapper.selectOne(new QueryWrapper<Hospital>().eq("name", params.get("hospitalName") ));
        Department department = departmentMapper.selectOne(new QueryWrapper<Department>().eq("name", params.get("departmentName")).eq("hospitalId", hospital.getId()));
        Map<String, Object> res = new HashMap<>();
        if ( department != null){
            params.put("departmentId", department.getId());
            params.put("name", (String)params.get("doctorName"));
            Doctor doctor = dozerUtil.map(params, Doctor.class);
            doctorMapper.insert(doctor);
            res.put("code", 200);
            res.put("message", "添加成功");
        }else{
            res.put("code", 1);
            res.put("message", "请填写正确的医院部门");
        }
        return res;
    }

    @Override
    public Map<String, Object> updateDoctorById(Map<String, Object> params) {
        Hospital hospital = hospitalMapper.selectOne(new QueryWrapper<Hospital>().eq("name", params.get("hospitalName") ));
        Department department = departmentMapper.selectOne(new QueryWrapper<Department>().eq("name", params.get("departmentName")).eq("hospitalId", hospital.getId()));
        Map<String, Object> res = new HashMap<>();
        if ( department != null){
            params.put("departmentId", department.getId());
            params.put("name", (String)params.get("doctorName"));
            Doctor doctor = dozerUtil.map(params, Doctor.class);
            doctorMapper.updateById(doctor);
            res.put("code", 200);
            res.put("message", "添加成功");
        }else{
            res.put("code", 1);
            res.put("message", "请填写正确的医院部门");
        }
        return res;
    }
}




