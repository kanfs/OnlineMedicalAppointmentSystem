package com.kanfs.omas.service;

import com.kanfs.omas.pojo.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author 方盛
* @description 针对表【message】的数据库操作Service
* @createDate 2023-12-12 15:38:16
*/
public interface MessageService extends IService<Message> {
    Map<String, Object> getMessageList(Map<String, Object> params);

    int viewMessage(int id);

    int saveMessage(Message build);
}
