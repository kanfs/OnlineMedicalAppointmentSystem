package com.kanfs.omas.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Properties;

/**
 * 发邮件工具类
 */
@Component
public final class EmailUtil {
    @Resource
    private JavaMailSender javaMailSender;

    /**
     * 发送邮件
     * @param to 收件人邮箱
     * @param text 邮件正文
     * @param title 标题
     */
    public boolean sendEmail(String to, String text, String title){
        try {

            // 创建邮件消息
            SimpleMailMessage message = new SimpleMailMessage();
            // 设置发件人
            message.setFrom("OMAS智能预约服务平台 <okmijngenesee@qq.com>");
            message.setSentDate(new Date());
            message.setTo(to);
            // 标题
            message.setSubject(title);
            // 内容
            message.setText(text);
            // 发送邮件
            javaMailSender.send(message);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}


