package com.kanfs.omas.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtil implements InitializingBean {

    /**
     * 地域节点
     */
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    /**
     * id
     */
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    /**
     * 秘钥
     */
    @Value("${aliyun.oss.secret}")
    private String secret;
    /**
     * bucket
     */
    @Value("${aliyun.oss.bucket}")
    private String bucket;


    public static String ENDPOINT;
    public static String ACCESS_KEY_ID;
    public static String SECRECT;
    public static String BUCKET;

    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT = endpoint;
        ACCESS_KEY_ID = accessKeyId;
        SECRECT = secret;
        BUCKET = bucket;
    }
}
