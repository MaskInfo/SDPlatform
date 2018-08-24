package cn.org.upthink.exception;

import lombok.AllArgsConstructor;

/**
 * Copyright (C), 2018-2018
 * FileName: BussinessException
 * Author: Connie
 * Date: 2018/8/24 11:06
 * Description:
 */
@AllArgsConstructor
public class BussinessException extends RuntimeException {
    private String code;

    private String msg;

}
