package com.kanfs.omas.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kanfs.omas.pojo.Hospital;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【department】的数据库操作Service
* @createDate 2023-12-12 15:38:16
*/
public interface DepartmentService extends IService<Department> {
    public List<String> getDepartmentNames(String hospitalName);

    Map<String, Object> selectPage(Page<Department> departmentPage, Map<String, Object> searchObj);

    Map<String, Object> getNames(long departmentId);
}
