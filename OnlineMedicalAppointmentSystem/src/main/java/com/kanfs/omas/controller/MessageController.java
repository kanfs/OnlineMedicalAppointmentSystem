package com.kanfs.omas.controller;

import com.kanfs.omas.pojo.Message;
import com.kanfs.omas.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/mas/message-list")
    public Map<String, Object> getMessageList(@RequestParam Map<String, Object> params){
        Map<String, Object> result = messageService.getMessageList(params);
        return result;
    }

    @GetMapping("/mas/view-message")
    public int viewMessage(@RequestParam int id){
        return messageService.viewMessage(id);
    }
}
