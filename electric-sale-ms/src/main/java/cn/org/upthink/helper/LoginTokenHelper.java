package cn.org.upthink.helper;

import cn.org.upthink.entity.User;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.persistence.mybatis.util.StringUtils;
import cn.org.upthink.service.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2018-2018
 * FileName: LoginTokenHelper
 * Author: Connie
 * Date: 2018/8/23 18:16
 * Description:
 */
public enum LoginTokenHelper {
    INSTANCE;

    private static final int ACCESSTOKENEXPIRESIN = 3;

    public static final String TOKEN_USER_CACHE = "token:user:cache:";

    /**
     * 设置session
     */
    public String setSession(StringRedisTemplate stringRedisTemplate, String sessionKey, String openId, UserFormDTO userFormDTO){
        String val = String.format("%s#%s", sessionKey, openId);
        String accessToken = getAccessToken();
        deleteOldToken(stringRedisTemplate, accessToken);
        stringRedisTemplate.boundValueOps(accessToken).set(val);
        stringRedisTemplate.expire(accessToken, ACCESSTOKENEXPIRESIN, TimeUnit.DAYS);

        stringRedisTemplate.boundValueOps(TOKEN_USER_CACHE+accessToken).set(JSON.toJSONString(userFormDTO));
        stringRedisTemplate.expire(TOKEN_USER_CACHE+accessToken, ACCESSTOKENEXPIRESIN, TimeUnit.DAYS);

        return accessToken;
    }

    private void deleteOldToken(StringRedisTemplate stringRedisTemplate, String accessToken) {
        stringRedisTemplate.delete(accessToken);
        stringRedisTemplate.delete(TOKEN_USER_CACHE+accessToken);
    }

    private String getAccessToken(){
        return UUID.randomUUID().toString();
    }

    public UserFormDTO getUserInfo(StringRedisTemplate stringRedisTemplate, HttpServletRequest request){
        String accessToken = request.getHeader("accessToken");

        if(StringUtils.isNotBlank(accessToken)){
            String userString = stringRedisTemplate.boundValueOps(TOKEN_USER_CACHE + accessToken).get();
            return JSON.parseObject(userString, UserFormDTO.class);
        }

        return null;
    }

    public String getOpenId(HttpServletRequest request, StringRedisTemplate stringRedisTemplate){
        String accessToken = request.getHeader("accessToken");
        if(StringUtils.isNotBlank(accessToken)){
            String val = stringRedisTemplate.boundValueOps(accessToken).get();
            return val.split("#")[1];
        }
        return null;
    }
}
