package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.pojo.AdminHospital;
import com.kanfs.omas.service.AdminHospitalService;
import com.kanfs.omas.mapper.AdminHospitalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 方盛
* @description 针对表【admin_hospital】的数据库操作Service实现
* @createDate 2024-03-15 23:45:00
*/
@Service
public class AdminHospitalServiceImpl extends ServiceImpl<AdminHospitalMapper, AdminHospital>
    implements AdminHospitalService {
    @Autowired
    private AdminHospitalMapper adminHospitalMapper;

    @Override
    public Long findHospitalIdByAdminId(int id) {
        return adminHospitalMapper.findHospitalIdByAdminId(id);
    }
}




