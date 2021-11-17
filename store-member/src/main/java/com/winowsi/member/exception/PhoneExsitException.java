package com.winowsi.member.exception;



/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/4 10:04
 */

public class PhoneExsitException extends RuntimeException {
    public PhoneExsitException() {
        super("手机号已被注册");
    }
}
