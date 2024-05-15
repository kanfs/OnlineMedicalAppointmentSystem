package com.kanfs.omas.utils;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 生成随机验证码
@Component
public class RandomUtil {
    @Autowired
    private Producer checkCode;

    public String getCheckCode() {
        return checkCode.createText();
    }
}
