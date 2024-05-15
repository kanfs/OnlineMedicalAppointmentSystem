package com.kanfs.omas.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.Vo.DepartmentVo;
import com.kanfs.omas.pojo.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kanfs.omas.pojo.Hospital;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【department】的数据库操作Mapper
* @createDate 2023-12-12 15:38:16
* @Entity com.kanfs.pojo.omas.Department
*/
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    // 根据医院名字得到下属部门名字
    List<String> findDepartmentNames(String hospitalName);

    List<DepartmentVo> selectPageList(Page<Department> departmentPage, Map<String, Object> searchObj);

    int getTotalRowCount();
}




