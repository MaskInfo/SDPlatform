package cn.org.upthink.service;

import cn.org.upthink.common.util.*;
import cn.org.upthink.converter.Xml2MapConverter;
import cn.org.upthink.entity.Question;
import cn.org.upthink.entity.User;
import cn.org.upthink.helper.LoginTokenHelper;
import cn.org.upthink.model.dto.PayFormDto;
import cn.org.upthink.model.dto.PayNotifyDto;
import cn.org.upthink.model.dto.UserFormDTO;
import cn.org.upthink.model.type.PayTypeEnum;
import cn.org.upthink.util.HttpClientUtils;
import cn.org.upthink.util.WechatUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private UserService userService;

    @Transactional(readOnly = false)
    public Map<String, String> preparePay(HttpServletRequest request, PayFormDto payFormDto) throws Exception {
        //支付类型为提问时 初始化问题id 便于封装attach
        String operationId;
        if(PayTypeEnum.ASK.name().equals(payFormDto.getPayType())){
            operationId = String.valueOf(new Date().getTime());
        }else{
            operationId = payFormDto.getCourseId();
        }
        //拼接参数
        String params = this.getParams(request, payFormDto, operationId);
        System.out.println(params);

        //请求 获取prepare_id
        String result = HttpClientUtils.INSTANCE.sendPost(preparePayUrl, params, HttpClientUtils.XML);
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
        if (payType.equals(PayTypeEnum.ASK.name())) {
            questionService.save(operationId, request, payFormDto);
        } else {
            //do nothing
        }

        return map;
    }

    private String getParams(HttpServletRequest request, PayFormDto payFormDto, String operationId) throws Exception {
        Map<String, String> map = new HashMap<>();
        List<String> keyList = Arrays.asList("appid", "attach", "body", "mch_id", "nonce_str", "notify_url", "openid", "out_trade_no", "spbill_create_ip", "total_fee", "trade_type");
        map.put("appid", appId);
        map.put("body", "售电小程序-支付");
        map.put("mch_id", shopNumber);
        map.put("nonce_str", RandomStringUtils.random(12, true, true));
        map.put("attach", WechatUtil.setAttachData(stringRedisTemplate, payFormDto, operationId, map.get("nonce_str")));
        map.put("notify_url", "http://119.29.161.236:8081/v1/pay/callback");
        map.put("out_trade_no", RandomStringUtils.random(12, true, true));
        map.put("openid", LoginTokenHelper.INSTANCE.getOpenId(request, stringRedisTemplate));
        map.put("spbill_create_ip", "192.168.1.194");//TODO
        map.put("total_fee", payFormDto.getFee());
        map.put("trade_type", "JSAPI");
        map.put("sign", getSign(map, keyList));
        return Xml2MapConverter.map2XmlString(map);
    }

    private String getSign(Map<String, String> map, List<String> keyList) {
        StringBuilder sb = new StringBuilder();
        keyList.forEach(key -> {
            if(StringUtils.isNotBlank(map.get(key))){
                sb.append(key).append("=").append(map.get(key)).append("&");
            }
        });

        sb.append("key=").append(key);

        String sign = MD5.getMD5(sb.toString()).toUpperCase();
        return sign;
    }

    @Transactional(readOnly = false)
    public String callback(PayNotifyDto payNotifyDto) throws Exception {
        System.out.println(payNotifyDto);
        if (WechatUtil.SUCCESS.equals(payNotifyDto.getReturn_code())) {
            //TODO 签名校验
            Map<String, String> attachData = WechatUtil.getAttachData(stringRedisTemplate, payNotifyDto.getNonce_str(), payNotifyDto.getAttach());
            if(attachData.isEmpty()){
                return WechatUtil.returnXmlData(WechatUtil.FAIL, "商家数据包为空");
            }

            //校验订单总额
            if(!attachData.get("fee").equals(payNotifyDto.getTotal_fee())){
                return WechatUtil.returnXmlData(WechatUtil.FAIL,"订单总额不一致");
            }

            //根据operationId进行操作
            String payType = attachData.get("payType");
            String operationId = attachData.get("operationId");
            if (payType.equals(PayTypeEnum.ASK.name())) {
                Question question = questionService.get(operationId);
                if(Objects.isNull(question)){
                    question.setPay(true);
                    questionService.save(question);
                }
            } else {
                String openid = payNotifyDto.getOpenid();
                User user = userService.getUserByOpenId(openid);
                if(Objects.isNull(user)){
                    return WechatUtil.returnXmlData(WechatUtil.FAIL, "openId无效");
                }

                courseService.bind(user.getId(), operationId);
            }

            return WechatUtil.returnXmlData(WechatUtil.SUCCESS, "OK");
        }

        return WechatUtil.returnXmlData(WechatUtil.FAIL,"return_code不为success");
    }
}
