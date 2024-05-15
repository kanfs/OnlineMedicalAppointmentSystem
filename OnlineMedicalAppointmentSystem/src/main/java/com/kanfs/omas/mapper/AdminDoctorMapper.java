package com.kanfs.omas.mapper;

import com.kanfs.omas.pojo.AdminDoctor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 方盛
* @description 针对表【admin_doctor】的数据库操作Mapper
* @Entity com.kanfs.omas.pojo.AdminDoctor
*/
@Mapper
public interface AdminDoctorMapper extends BaseMapper<AdminDoctor> {

    Long findDoctorIdByAdminId(int id);
}




