package com.kanfs.omas.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Administer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【administer】的数据库操作Mapper
* @createDate 2023-12-12 15:38:16
* @Entity com.kanfs.pojo.omas.Administer
*/
@Mapper
public interface AdministerMapper extends BaseMapper<Administer> {

    Administer getAdminByUserName(String username);

    Administer selectAdminById(int id);

    List<Administer> selectPageList(Page<Administer> administerPage, @Param("searchObj")Map searchObj);

    int getTotalRowCount();

    int insertAdmin(Map params);

    int updateAdmin(Map params);

    int updateAdminStatus(int id, boolean status);

    int deleteAdminById(int id);
}




