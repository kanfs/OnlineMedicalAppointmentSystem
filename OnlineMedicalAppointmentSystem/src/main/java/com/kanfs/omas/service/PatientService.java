package com.kanfs.omas.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 方盛
* @description 针对表【patient】的数据库操作Service
*/
public interface PatientService extends IService<Patient> {

    Map<String, Object> getPatientList(Map params);

    Map<String, Object> registerPatient(Map params);

    Map<String, Object> updatePatient(Map map);

    Map<String, Object> getPatientById(int id);

    Map<String, Object> deletePatient(int id);

    Map<String, Object> authPatient(int id);
}
