package cn.org.upthink.inteceptor;

import cn.org.upthink.exception.BussinessException;
import cn.org.upthink.model.ResponseConstant;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Copyright (C), 2018-2018
 * FileName: RequestHeaderInteceptor
 * Author: Connie
 * Date: 2018/8/24 11:32
 * Description:
 */
public class RequestHeaderInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader("accessToken");

        String val = stringRedisTemplate.boundValueOps(accessToken).get();
        if(StringUtils.isBlank(val)){
            throw new BussinessException(ResponseConstant.ACCESSTOKEN_OVERDUE.getCode(), ResponseConstant.ACCESSTOKEN_OVERDUE.getMsg());
        }

        return super.preHandle(request, response, handler);
    }
}
