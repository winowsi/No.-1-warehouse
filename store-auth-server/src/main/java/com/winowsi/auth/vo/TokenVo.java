package com.winowsi.auth.vo;

import lombok.Data;


/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/11 16:03
 */
@Data
public class TokenVo {
    /**
     * 认证令牌
     */
    private String access_token;
    /**
     * 认证方式
     */
    private String remind_in;
    /**
     * 令牌的过期时间
     */
    private long expires_in;
    /**
     * 用户名
     */
    private String uid;
    /**
     *
     */
    private String isRealName;
}
