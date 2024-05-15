package com.kanfs.omas.service;

import com.kanfs.omas.pojo.AdminHospital;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 方盛
* @description 针对表【admin_hospital】的数据库操作Service
* @createDate 2024-03-15 23:45:00
*/
public interface AdminHospitalService extends IService<AdminHospital> {

    Long findHospitalIdByAdminId(int id);
}
