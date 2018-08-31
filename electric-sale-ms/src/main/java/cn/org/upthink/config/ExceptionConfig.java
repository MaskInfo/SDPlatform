package cn.org.upthink.config;

import cn.org.upthink.exception.CustomMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Copyright (C), 2018-2018
 * FileName: ExceptionConfig
 * Author: Connie
 * Date: 2018/8/30 20:44
 * Description:
 */
@Configuration
public class ExceptionConfig {
    @Bean
    public CustomMapper customMapper(){
        return new CustomMapper();
    }

    @Bean
    public MappingJackson2HttpMessageConverter converter(){
        return new MappingJackson2HttpMessageConverter(customMapper());
    }
}
