package cn.org.upthink.model.type;

import lombok.Getter;

/**
 * Copyright (C), 2018-2018
 * FileName: PayTypeEnum
 * Author: Connie
 * Date: 2018/8/28 11:36
 * Description:
 */
@Getter
public enum PayTypeEnum {
    COURSE("course"),
    ASK("ask")
    ;
    private String value;

    PayTypeEnum(String value) {
        this.value = value;
    }
}
