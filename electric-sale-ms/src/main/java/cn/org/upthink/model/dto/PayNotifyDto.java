package cn.org.upthink.model.dto;

import cn.org.upthink.converter.Xml2MapConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2018-2018
 * FileName: PayNotifyDto
 * Author: Connie
 * Date: 2018/8/29 14:17
 * Description:用于包装支付回调参数
 */
@Getter
@Setter
@ToString
@XmlRootElement(name = "xml")
public class PayNotifyDto {
    private String return_code;

    private String return_msg;

    private String appid;

    private String mch_id;

    private String device_info;

    private String nonce_str;

    private String sign;

    private String sign_type;

    private String result_code;

    private String err_code;

    private String err_code_des;

    private String openid;

    private String is_subscribe;

    private String trade_type;

    private String bank_type;

    private String total_fee;

    private String settlement_total_fee;

    private String fee_type;

    private String cash_fee_type;

    private String cash_fee;

    private String transaction_id;

    private String out_trade_no;

    private String attach;

    private String time_end;

}
