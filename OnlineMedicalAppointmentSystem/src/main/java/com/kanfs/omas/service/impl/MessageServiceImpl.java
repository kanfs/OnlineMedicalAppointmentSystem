package com.kanfs.omas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanfs.omas.pojo.Message;
import com.kanfs.omas.service.MessageService;
import com.kanfs.omas.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 方盛
* @description 针对表【message】的数据库操作Service实现
* @createDate 2024-01-05 15:25:23
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{
    @Autowired
    private MessageMapper messageMapper;
    @Override
    public Map<String, Object> getMessageList(Map<String, Object> params) {
        Map<String, Object> res = new HashMap<String, Object>();
        params.put("firstLimit", (Integer.parseInt((String)params.get("page"))-1)*10 );
        Map<String, Object> messageMap = messageMapper.getMessageList(params);
        List<Map<String, Object>> messageList = new ArrayList<Map<String, Object>>();
        for( Map.Entry<String, Object> entry: messageMap.entrySet())
            messageList.add((Map<String, Object>)entry.getValue());
        res.put("messageList", messageList);
        res.put("messageCount", messageMapper.getTotalRowCount());
        return res;
    }

    @Override
    public int viewMessage(int id) {
        return messageMapper.viewMessage(id);
    }

    @Override
    public int saveMessage(Message message) {
        return messageMapper.insert(message);
    }
}




