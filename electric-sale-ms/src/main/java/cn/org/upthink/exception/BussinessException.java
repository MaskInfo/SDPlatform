package cn.org.upthink.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Copyright (C), 2018-2018
 * FileName: BussinessException
 * Author: Connie
 * Date: 2018/8/24 11:06
 * Description:
 */
@AllArgsConstructor
@Getter
public class BussinessException extends RuntimeException {
    private String code;

    private String msg;

}
