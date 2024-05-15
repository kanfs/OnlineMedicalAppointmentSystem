package com.kanfs.omas.mapper;

import com.kanfs.omas.pojo.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 方盛
* @description 针对表【record】的数据库操作Mapper
* @createDate 2023-12-12 15:38:16
* @Entity com.kanfs.pojo.omas.Record
*/
@Mapper
public interface RecordMapper extends BaseMapper<Record> {

}




