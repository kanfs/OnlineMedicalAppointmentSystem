package com.kanfs.omas.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.Vo.DoctorVo;
import com.kanfs.omas.pojo.Doctor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 方盛
* @description 针对表【doctor】的数据库操作Service
* @createDate 2023-12-12 15:38:16
*/
public interface DoctorService extends IService<Doctor> {
    Map<String, Object> getMatchedDoctors(Map<String, Object> params);

    Map<String, Object> getDoctorInfo(long id);

    Map<String, Object> selectPage(Page<Doctor> doctorPage, Map<String, Object> searchObj);

    DoctorVo getDoctorById(long id);

    Map<String, Object> saveDoctor(Map<String, Object> params);

    Map<String, Object> updateDoctorById(Map<String, Object> params);
}
