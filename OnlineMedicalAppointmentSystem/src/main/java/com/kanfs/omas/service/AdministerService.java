package com.kanfs.omas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanfs.omas.pojo.Administer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【administer】的数据库操作Service
* @createDate 2023-12-12 15:38:16
*/
public interface AdministerService extends IService<Administer> {

    Map<String, Object> login(Map params);

    Map<String, Object> getUserInfo(int id);


    Map<String, Object> getPageList(Page<Administer> administerPage, Map searchObj);

    Administer getAdminById(int id);

    Map<String, Object> insertAdmin(Map params);

    Map<String, Object> updateAdmin(Map params);

    Map<String, Object> updateAdminStatus(int id, boolean status);

    Map<String, Object> deleteAdminById(int id);
}
