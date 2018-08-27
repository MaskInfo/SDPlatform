package cn.org.upthink.config;

import cn.org.upthink.inteceptor.RequestHeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Copyright (C), 2018-2018
 * FileName: InteceptorConfig
 * Author: Connie
 * Date: 2018/8/24 11:37
 * Description:
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestHeaderInterceptor()).addPathPatterns("/**").excludePathPatterns("/login");
    }

    @Bean
    public RequestHeaderInterceptor requestHeaderInterceptor(){
        return new RequestHeaderInterceptor();
    }
}
