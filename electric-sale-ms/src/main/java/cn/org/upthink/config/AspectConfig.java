package cn.org.upthink.config;

import cn.org.upthink.aspect.RequestLoggerAspect;
import cn.org.upthink.web.filter.RequestLoggerFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {
    @Bean
    public RequestLoggerAspect requestLoggerAspect(){
        return new RequestLoggerAspect();
    }
    @Bean
    public RequestLoggerFilter requestLoggerFilter(){
        return new RequestLoggerFilter();
    }
}
