package cn.org.upthink.web.controller.advice;

import cn.org.upthink.anno.RequestLogging;
import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.exception.BussinessException;
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

    @RequestLogging
    @ExceptionHandler({IllegalStateException.class,NullPointerException.class,IllegalArgumentException.class})
    @ResponseBody
    public BaseResult<?> handlerIllegalStateException(Exception e){
        return getErrorResult(ResponseConstant.INVALID_PARAM.getCode(), e.getMessage());
    }

    @RequestLogging
    @ExceptionHandler(BussinessException.class)
    @ResponseBody
    public BaseResult<?> handlerBussinessException(BussinessException e){
        return getErrorResult(e.getCode(), e.getMsg());
    }

    @RequestLogging
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult<?> handlerException(Exception e){
        return getErrorResult(ResponseConstant.HANDLER_EXCEPTION.getCode(), e.getMessage());
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
