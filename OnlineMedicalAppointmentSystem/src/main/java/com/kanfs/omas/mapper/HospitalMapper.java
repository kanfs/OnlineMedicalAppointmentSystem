package com.kanfs.omas.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Hospital;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【hospital】的数据库操作Mapper
* @createDate 2023-12-12 15:38:16
* @Entity com.kanfs.pojo.omas.Hospital
*/
@Mapper
public interface HospitalMapper extends BaseMapper<Hospital> {
    // 查询平台医院数量
    Integer countHospitalNum();
    // 查询平台医生数量
    Integer countDoctorNum();
    // 查询大医院名 （按医生数量降序
    List<String> searchHospitalName(String province, String city);
    // 根据名字查询医院
    List<String> searchHospitalByName(Map<String, Object> params);

    List<Hospital> selectPageList(Page<Hospital> hospitalPage, Map<String, Object> searchObj);

    int getTotalRowCount();

    Long getHospitalIdByName(String name);
}




