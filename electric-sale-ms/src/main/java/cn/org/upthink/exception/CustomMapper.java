package cn.org.upthink.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Copyright (C), 2018-2018
 * FileName: CustomMapper
 * Author: Connie
 * Date: 2018/8/30 20:42
 * Description:
 */
public class CustomMapper extends ObjectMapper {
    public CustomMapper(){
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
