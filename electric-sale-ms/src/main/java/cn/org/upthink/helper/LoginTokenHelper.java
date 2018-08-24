package cn.org.upthink.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2018-2018
 * FileName: LoginTokenHelper
 * Author: Connie
 * Date: 2018/8/23 18:16
 * Description:
 */
public abstract class LoginTokenHelper {
    @Autowired
    private static StringRedisTemplate stringRedisTemplate;

    private static final int ACCESSTOKENEXPIRESIN = 3;

    /**
     * 设置session
     */
    public static String setSession(String sessionKey, String openId){
        String val = String.format("%s#%s", sessionKey, openId);
        String accessToken = getAccessToken();
        deleteOldToken(accessToken);
        stringRedisTemplate.boundValueOps(accessToken).set(val);
        stringRedisTemplate.expire(accessToken, ACCESSTOKENEXPIRESIN, TimeUnit.DAYS);

        return accessToken;
    }

    private static void deleteOldToken(String key) {
        stringRedisTemplate.delete(key);
    }

    private static String getAccessToken(){
        return UUID.randomUUID().toString();
    }
}
