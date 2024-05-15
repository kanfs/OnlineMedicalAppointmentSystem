package com.kanfs.omas.mapper;

import com.kanfs.omas.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 方盛
* @description 针对表【menu(菜单表)】的数据库操作Mapper

* @Entity com.kanfs.omas.pojo.menu
*/
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findListByAdminId(int id);
}




