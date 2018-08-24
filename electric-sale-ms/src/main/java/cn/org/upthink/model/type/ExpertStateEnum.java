package cn.org.upthink.model.type;

import lombok.Getter;

/**
 * Copyright (C), 2018-2018
 * FileName: ExpertStateEnum
 * Author: Connie
 * Date: 2018/8/24 17:35
 * Description:
 */
@Getter
public enum ExpertStateEnum {
    WAIT_AUDIT(0,"待审核"),
    AUDIT(1,"已通过"),
    NOT_AUDIT(2,"已拒绝")

    ;
    private Integer stateCode;

    private String state;

    ExpertStateEnum(Integer stateCode, String state) {
        this.stateCode = stateCode;
        this.state = state;
    }
}
