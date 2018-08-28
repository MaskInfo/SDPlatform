package cn.org.upthink.service;

import cn.org.upthink.common.util.HttpClientUtil;
import cn.org.upthink.common.util.IpUtil;
import cn.org.upthink.common.util.MD5;
import cn.org.upthink.common.util.RandomStrUtils;
import cn.org.upthink.converter.Xml2MapConverter;
import cn.org.upthink.entity.Question;
import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.dto.PayFormDto;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.model.type.PayTypeEnum;
import cn.org.upthink.util.HttpClientUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Copyright (C), 2018-2018
 * FileName: PayService
 * Author: Connie
 * Date: 2018/8/28 10:52
 * Description:
 */
@Service
@Transactional
public class PayService {

    @Value("${wechat.APPID}")
    private String appId;
    @Value("${wechat.APPSECRET}")
    private String appSecret;
    @Value("${wechat.shopNumber}")
    private String shopNumber;
    @Value("${wechat.key}")
    private String key;
    @Value("${wechat.perparePayUrl}")
    private String preparePayUrl;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CourseService courseService;

    @Transactional(readOnly = false)
    public Map<String, String> preparePay(HttpServletRequest request, PayFormDto payFormDto) throws Exception {
        //拼接参数
        String params = this.getParams(request, payFormDto);
        System.out.println(params);

        //请求 获取prepare_id
        String result = HttpClientUtils.INSTANCE.sendPost(preparePayUrl, params, HttpClientUtils.XML);
        System.out.println(result);
        Map<String, String> retMap = Xml2MapConverter.readStringXmlOut(result);
        System.out.println(retMap);

        //签名
        Map<String, String> map = new HashMap<>();
        List<String> keyList = Arrays.asList("appId", "nonceStr", "package", "signType", "timeStamp");
        map.put("appId", retMap.get("appid"));
        map.put("nonceStr", retMap.get("nonce_str"));
        map.put("signType", "MD5");
        map.put("timeStamp", String.valueOf(new Date().getTime()));
        map.put("package", "prepay_id=" + retMap.get("prepay_id"));
        map.put("paySign", getSign(map, keyList));

        //生成订单
        String payType = payFormDto.getPayType();
        if(payType.equals(PayTypeEnum.ASK.getValue())){
            UserFormDTO userInfo = LoginTokenHelper.INSTANCE.getUserInfo(stringRedisTemplate, request);
            questionService.save(userInfo.getUserId(), payFormDto.getQues_title(), payFormDto.getQues_detail());
        }else{
            //TODO
        }

        return map;
    }

    private String getParams(HttpServletRequest request, PayFormDto payFormDto) {
        Map<String, String> map = new HashMap<>();
        List<String> keyList = Arrays.asList("appid", "attach", "body", "mch_id", "nonce_str", "notify_url", "openid", "out_trade_no", "spbill_create_ip", "total_fee", "trade_type");
        map.put("appid", appId);
        map.put("attach", payFormDto.getPayType());
        map.put("body", "售电小程序-支付");
        map.put("mch_id", shopNumber);
        map.put("nonce_str", RandomStringUtils.random(12, true, true));
        map.put("notify_url", "http://www.baidu.com");
        map.put("out_trade_no", RandomStringUtils.random(12, true, true));
        map.put("openid", LoginTokenHelper.INSTANCE.getOpenId(request, stringRedisTemplate));
        map.put("spbill_create_ip", "110.65.96.101");//TODO
        map.put("total_fee", payFormDto.getFee());
        map.put("trade_type", "JSAPI");
        map.put("sign", getSign(map, keyList));
        return Xml2MapConverter.map2XmlString(map);
    }

    private String getSign(Map<String, String> map, List<String> keyList) {
        StringBuilder sb = new StringBuilder();
        keyList.forEach(key -> {
            sb.append(key).append("=").append(map.get(key)).append("&");
        });
        System.out.println(sb.toString());

        sb.append("key=").append(key);

        String sign = MD5.getMD5(sb.toString()).toUpperCase();
        return sign;
    }

    @Transactional(readOnly = false)
    public void callback(HttpServletRequest request, String payType, String id) {
        if(payType.equals(PayTypeEnum.ASK.getValue())){
            Question question = questionService.get(id);
            question.setPay(true);
            questionService.save(question);
        }else{
            //TODO 绑定课程
            //todo courseService.bind(userid,courseid);
        }
    }
}
