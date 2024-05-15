package com.kanfs.omas.mapper;

import com.kanfs.omas.pojo.AdminHospital;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 方盛
* @description 针对表【admin_hospital】的数据库操作Mapper
* @createDate 2024-03-15 23:45:00
* @Entity com.kanfs.omas.pojo.AdminHospital
*/
@Mapper
public interface AdminHospitalMapper extends BaseMapper<AdminHospital> {

    Long findHospitalIdByAdminId(int adminId);
}




