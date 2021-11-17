package com.winowsi.member.vo;

import lombok.Data;

import java.io.Serializable;


/**
 * @description: 会员注册 Vo
 * @author zhaoYao
 * @Time: 2021/11/3 17:12
 */
@Data
public class MemberRegisterVo implements Serializable {
    private  String userName;
    private  String password;
    private  String phone;
}
