package com.kanfs.omas.mapper;

import com.kanfs.omas.pojo.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【message】的数据库操作Mapper
* @createDate 2023-12-12 15:38:16
* @Entity com.kanfs.omas.pojo.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @MapKey("id")
    Map<String, Object> getMessageList(Map<String, Object> params);

    int getTotalRowCount();

    int viewMessage(int id);
}




