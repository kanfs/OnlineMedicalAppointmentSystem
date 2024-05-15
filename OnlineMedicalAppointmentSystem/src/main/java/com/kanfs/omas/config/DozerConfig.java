package com.kanfs.omas.config;

import com.github.dozermapper.core.DozerBeanMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.kanfs.omas.utils.DozerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class DozerConfig {

    @Bean
    public DozerUtil getDozerUtil() {
        DozerUtil dozerUtil = new DozerUtil(mapper());
        return dozerUtil;
    }
    @Bean
    public Mapper mapper() {
        return DozerBeanMapperBuilder.buildDefault();
    }
}
