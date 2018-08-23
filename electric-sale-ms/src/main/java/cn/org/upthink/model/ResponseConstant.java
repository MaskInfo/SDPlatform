package cn.org.upthink.model;

import lombok.Getter;

@Getter
public enum ResponseConstant {
    OK("200","正常"),
    INVALID_PARAM("406","非法参数"),
    HANDLER_EXCEPTION("500","内部错误");

    private String code;
    private String msg;

    ResponseConstant(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //todo  可自行添加
}