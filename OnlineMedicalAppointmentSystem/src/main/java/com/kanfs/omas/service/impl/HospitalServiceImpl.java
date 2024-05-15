package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.mapper.DepartmentMapper;
import com.kanfs.omas.pojo.Department;
import com.kanfs.omas.service.HospitalService;
import com.kanfs.omas.pojo.Hospital;
import com.kanfs.omas.mapper.HospitalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【hospital】的数据库操作Service实现
* @createDate 2023-12-12 15:38:16
*/
@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital>
    implements HospitalService {
    @Autowired
    HospitalMapper hospitalMapper;
    @Autowired
    DepartmentMapper departmentMapper;


    @Override
    public Map<String, Object> getBasicInfo() {
        Map<String, Object> res = new HashMap<>();
        res.put("hospitalNum", hospitalMapper.countHospitalNum());
        res.put("doctorNum", hospitalMapper.countDoctorNum());
        return res;
    }

    @Override
    public List<String> getHospitalName(String province, String city) {
        return hospitalMapper.searchHospitalName(province, city);
    }

    @Override
    public List<String> getHospitalByName(Map<String, Object> params) {
        return hospitalMapper.searchHospitalByName(params);
    }

    @Override
    public Map<String, Object> selectPage(Page<Hospital> hospitalPage, Map<String, Object> searchObj) {
        Map<String, Object> res = new HashMap<>();
        List<Hospital> hospitals = hospitalMapper.selectPageList(hospitalPage, searchObj);
        List<Map<String, Object>> records = new ArrayList<>();
        for (Hospital hospital : hospitals){

            Map<String, Object> record = new HashMap<>();
            record.put("id", hospital.getId());
            record.put("name", hospital.getName());
            record.put("style", hospital.getStyle());
            record.put("level", hospital.getLevel());
            record.put("type", hospital.getType());
            record.put("address", hospital.getAddress());
            record.put("telephone", hospital.getTelephone());
            record.put("intro", hospital.getIntro());
            record.put("departmentList", departmentMapper.selectList(new QueryWrapper<Department>().eq("hospitalId", hospital.getId())));
            records.add(record);
        }
        res.put("records", records);
        res.put("total", hospitalMapper.getTotalRowCount());
        return res;
    }

    @Override
    public Long getHospitalIdByName(String name) {
        return hospitalMapper.getHospitalIdByName(name);
    }
}




