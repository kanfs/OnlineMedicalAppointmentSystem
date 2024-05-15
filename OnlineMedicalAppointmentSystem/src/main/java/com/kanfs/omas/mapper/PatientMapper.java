package com.kanfs.omas.mapper;

import com.kanfs.omas.pojo.Patient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【patient】的数据库操作Mapper
* @Entity com.kanfs.omas.pojo.Patient
*/
@Mapper
public interface PatientMapper extends BaseMapper<Patient> {

    List<Patient> getPatientsByUserId(int userId);

    Patient findUserByIdentifyCardNumber(String identifyCardNumber);

    void insertPatient(Patient regPatient);

    Patient getPatientById(int id);

    int deletePatientById(int id);

    int updatePatient(Map map);

    int updatePatientStatusById(int id, boolean status);
}




