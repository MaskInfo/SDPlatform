package cn.org.upthink.aspect;

import cn.org.upthink.model.logger.RequestLogger;
import cn.org.upthink.model.logger.ResponseLogger;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * controllerAdvice 方法前后进行日志记录
 * 或者有RequestLogging注解的方法
 */
@Aspect
public class RequestLoggerAspect {
    protected static final Logger logger = LoggerFactory.getLogger(RequestLoggerAspect.class);

    @Around(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.ResponseBody)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("RequestLoggerAspect......");
        //do
        Object resp = joinPoint.proceed();
        //done 创建日志类
        RequestLogger requestLogger = RequestLogger.builder()
                .apiDesc(getApiDescByRequestSignature(joinPoint))
                .responseTime(new Date())
                .build().init();
        //保存日志
        System.out.println(requestLogger);
        logger.info("LOGGING------>\n Request: \n{} \n Response:\n {}",requestLogger,ResponseLogger.with(resp));
        return resp;
    }

    /**
     * 获取swagger  方法描述
     * @param joinPoint
     * @return
     */
    private String getApiDescByRequestSignature(ProceedingJoinPoint joinPoint) {
        if(joinPoint.getSignature() instanceof  MethodSignature){
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            if(apiOperation != null){
                return apiOperation.value();
            }
        }
        return "";
    }

}
