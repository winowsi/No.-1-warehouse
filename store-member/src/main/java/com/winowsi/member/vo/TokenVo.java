package com.winowsi.member.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/11 16:03
 */
@Data
public class TokenVo implements Serializable {
    private String access_token;
    private String remind_in;
    private String expires_in;
    private String uid;
    private String isRealName;
}
