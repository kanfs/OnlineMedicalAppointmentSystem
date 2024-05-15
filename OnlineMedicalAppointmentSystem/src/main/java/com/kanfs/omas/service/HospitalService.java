package com.kanfs.omas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Hospital;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【hospital】的数据库操作Service
* @createDate 2023-12-12 15:38:16
*/
public interface HospitalService extends IService<Hospital> {
    Map<String, Object> getBasicInfo();
    List<String> getHospitalName(String province, String city);

    List<String> getHospitalByName(Map<String, Object> params);

    Map<String, Object> selectPage(Page<Hospital> hospitalPage, Map<String, Object> searchObj);

    Long getHospitalIdByName(String name);
}
