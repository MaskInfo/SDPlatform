package cn.org.upthink.web.controller.advice;

import cn.org.upthink.anno.RequestLogging;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.model.ResponseConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常类处理
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({IllegalStateException.class,NullPointerException.class,IllegalArgumentException.class})
    @ResponseBody
    public BaseResult<?> handlerIllegalStateException(Exception e){
        logger.error(e.getMessage().toString());
        return getErrorResult(e.getMessage(), ResponseConstant.INVALID_PARAM.getCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult<?> handlerException(Exception e){
        logger.error(e.getMessage().toString());
        return getErrorResult(e.getMessage(),ResponseConstant.HANDLER_EXCEPTION.getCode());
    }

    private BaseResult<String> getErrorResult(String errorCode,String msg){
        BaseResult<String> errorRet = new BaseResult<>();
        errorRet.setCode(errorCode);
        errorRet.setContent(null);
        errorRet.setMessage(msg);
        errorRet.setSuccess(false);
        return errorRet;
    }
}
