package com.kanfs.omas.config;

import com.kanfs.omas.interceptor.CorsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    // 后端能够接收来自8080端口和9518端口的请求
    @Autowired
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //将CorsInterceptor拦截器添加进来
        registry.addInterceptor(corsInterceptor).addPathPatterns("/**");
    }

    //跨域请求
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").maxAge(1800).allowedOrigins("http://localhost:8080").allowCredentials(true);
        registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").maxAge(1800).allowedOriginPatterns("*").allowCredentials(true);
    }
}
