package com.kanfs.omas.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.Vo.DoctorVo;
import com.kanfs.omas.pojo.Doctor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【doctor】的数据库操作Mapper
* @createDate 2023-12-12 15:38:16
* @Entity com.kanfs.pojo.omas.Doctor
*/
@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {
    @MapKey("id")
    Map<String, Object> findMatchedDoctors(Map<String, Object> params);

    int getTotalRowCount();

    Doctor findDoctorById(long id);

    @MapKey("id")
    Map<String, Object> selectPageList(Page<Doctor> doctorPage, Map<String, Object> searchObj);


    DoctorVo getDoctorInfo(long id);
}




