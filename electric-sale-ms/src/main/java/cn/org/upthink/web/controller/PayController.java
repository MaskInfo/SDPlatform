package cn.org.upthink.web.controller;

import cn.org.upthink.common.dto.BaseResult;
import cn.org.upthink.model.ResponseConstant;
import cn.org.upthink.model.dto.PayFormDto;
import cn.org.upthink.model.type.PayTypeEnum;
import cn.org.upthink.service.PayService;
import cn.org.upthink.web.BaseController;
import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Copyright (C), 2018-2018
 * FileName: PayController
 * Author: Connie
 * Date: 2018/8/28 10:39
 * Description:
 */
@Api(value = "payApi", description = "支付Controller", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "/v1/pay")
public class PayController extends BaseController {
    @Autowired
    private PayService payService;

    @ApiOperation(value = "生成预支付订单", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/prepare", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public BaseResult preparePay(HttpServletRequest request, @RequestBody PayFormDto payFormDto) throws Exception {
        String payType = payFormDto.getPayType();
        if(StringUtils.isBlank(payType) || !EnumUtils.isValidEnum(PayTypeEnum.class, payType)){
            return getBaseResultSuccess(null, ResponseConstant.PAYTYPE_IS_NULL_OR_ERROR.getCode(), ResponseConstant.PAYTYPE_IS_NULL_OR_ERROR.getMsg());
        }

        Preconditions.checkState(StringUtils.isNotBlank(payFormDto.getFee()), ResponseConstant.INVALID_PARAM.getMsg());

        if(payType.equals(PayTypeEnum.ASK.getValue())){
            Preconditions.checkState(StringUtils.isNotBlank(payFormDto.getQues_detail()), ResponseConstant.INVALID_PARAM.getMsg());
            Preconditions.checkState(StringUtils.isNotBlank(payFormDto.getQues_title()), ResponseConstant.INVALID_PARAM.getMsg());
        }

        Map<String, String> ret = payService.preparePay(request, payFormDto);
        if(ret.isEmpty()){
            return getBaseResultSuccess(null, ResponseConstant.PREPARY_FAIL.getCode(), ResponseConstant.PREPARY_FAIL.getMsg());
        }
        return getBaseResultSuccess(ret, ResponseConstant.OK.getCode(), ResponseConstant.OK.getMsg());
    }

    @ApiOperation(value = "支付回调", notes = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/callback", produces = "application/json;charset=UTF-8", method = RequestMethod.PUT)
    public BaseResult callback(HttpServletRequest request, String payType, String id){
        if(StringUtils.isBlank(payType) || !EnumUtils.isValidEnum(PayTypeEnum.class, payType)){
            return getBaseResultSuccess(null, ResponseConstant.PAYTYPE_IS_NULL_OR_ERROR.getCode(), ResponseConstant.PAYTYPE_IS_NULL_OR_ERROR.getMsg());
        }

        Preconditions.checkState(StringUtils.isNotBlank(id), ResponseConstant.INVALID_PARAM.getMsg());

        payService.callback(request, payType, id);
        return getBaseResultSuccess(null, ResponseConstant.OK.getCode(), ResponseConstant.OK.getMsg());
    }

}
