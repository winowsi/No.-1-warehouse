package com.winowsi.member.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/4 16:47
 */
@Data
public class MemberLoginVo implements Serializable {
    private String accountUserName;
    private String password;
}
