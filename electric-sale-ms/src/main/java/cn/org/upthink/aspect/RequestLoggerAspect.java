package cn.org.upthink.aspect;

import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.model.ResponseConstant;
import cn.org.upthink.model.logger.RequestLogger;
import cn.org.upthink.model.logger.ResponseLogger;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
import cn.org.upthink.util.Jacksons;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * controllerAdvice 方法前后进行日志记录
 * 或者有RequestLogging注解的方法
 */
@Aspect
public class RequestLoggerAspect {
    protected static final Logger logger = LoggerFactory.getLogger(RequestLoggerAspect.class);

    @Around(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        //do
        Object resp = joinPoint.proceed();
        //done 创建日志类
        RequestLogger requestLogger = new RequestLogger(getApiDescByRequestSignature(joinPoint).value(), new Date());
        //保存日志
        System.out.println(resp.toString());
        BaseResult baseResult;
        if(MediaType.APPLICATION_JSON_UTF8_VALUE.equals(getApiDescByRequestSignature(joinPoint).produces())){
            baseResult = JSON.parseObject(resp.toString(), BaseResult.class);
        }else{
            baseResult = xml2BaseResult(resp.toString());
        }
        if(Arrays.asList(ResponseConstant.codeValues()).contains(baseResult.getCode())){
            logger.error("LOGGING | ERROR \n REQUEST | HEADER {} \n REQUEST | PARAM {} \n RESPONSE | RESULT {}",
                    requestLogger.getHeaders(),requestLogger.getRequestBody(), StringUtils.deleteWhitespace(resp.toString()));
        }else{
            logger.info("LOGGING | INFO \n REQUEST | HEADER {} \n REQUEST | PARAM {} \n RESPONSE | RESULT {}",
                    requestLogger.getHeaders(),requestLogger.getRequestBody(), StringUtils.deleteWhitespace(resp.toString()));
        }
        return resp;
    }

    /**
     * 获取swagger  方法描述
     * @param joinPoint
     * @return
     */
    private ApiOperation getApiDescByRequestSignature(ProceedingJoinPoint joinPoint) {
        if(joinPoint.getSignature() instanceof  MethodSignature){
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            if(apiOperation != null){
                return apiOperation;
            }
        }
        return null;
    }

    public BaseResult xml2BaseResult(String xml){
        try {
            Document document = DocumentHelper.parseText(xml);
            Element rootElement = document.getRootElement();
            String return_code = rootElement.element("return_code").getText();
            String return_msg = rootElement.element("return_msg").getText();
            return new BaseResult(return_code, return_msg);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new BaseResult();
    }

}
