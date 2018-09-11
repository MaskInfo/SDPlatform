package cn.org.upthink.util;

import cn.org.upthink.common.util.RSAEncrypt;
import cn.org.upthink.common.util.StringUtils;
import cn.org.upthink.converter.Xml2MapConverter;
import cn.org.upthink.model.dto.PayFormDto;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2018-2018
 * FileName: WechatUtil
 * Author: Connie
 * Date: 2018/8/29 14:36
 * Description:
 */
public class WechatUtil {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    /**
     * 返回回调数据
     *
     * @param code
     * @param msg
     * @return
     */
    public static String returnXmlData(String code, String msg) {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", code);
        map.put("return_msg", msg);
        return Xml2MapConverter.map2XmlString(map);
    }


    /**
     * 设置商家数据包
     *
     * @param stringRedisTemplate
     * @param payFormDto
     * @param operationId
     * @param nonceStr
     * @return
     * @throws Exception
     */
    public static String setAttachData(StringRedisTemplate stringRedisTemplate, PayFormDto payFormDto, String operationId, String nonceStr) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("payType", payFormDto.getPayType());
        map.put("operationId", operationId);
        map.put("fee", payFormDto.getFee());
        map.put("anserId",payFormDto.getAnswerId());
        String attach = JSON.toJSONString(map);
        return attach;
    }

    /**
     * 获取商家数据包
     *
     * @param stringRedisTemplate
     * @param nonceStr
     * @param attach
     * @return
     * @throws Exception
     */
    public static Map<String, String> getAttachData(StringRedisTemplate stringRedisTemplate, String nonceStr, String attach) throws Exception {
        if (StringUtils.isNotBlank(attach)) {
            return JSON.parseObject(attach, Map.class);
        }

        return null;
    }

}
