package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.pojo.AdminDoctor;
import com.kanfs.omas.service.AdminDoctorService;
import com.kanfs.omas.mapper.AdminDoctorMapper;
import org.springframework.stereotype.Service;

/**
* @author 方盛
* @description 针对表【admin_doctor】的数据库操作Service实现
*/
@Service
public class AdminDoctorServiceImpl extends ServiceImpl<AdminDoctorMapper, AdminDoctor>
    implements AdminDoctorService{

}




