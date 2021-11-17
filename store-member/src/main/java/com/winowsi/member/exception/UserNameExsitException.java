package com.winowsi.member.exception;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/4 10:04
 */

public class UserNameExsitException extends RuntimeException {
    public UserNameExsitException() {
        super("用户名已存在");
    }
}
