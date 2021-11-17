package com.winowsi.common.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-10-13 10:12:43
 */
@Data
public class MemberVo implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 会员等级id
     */
    private Long levelId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String header;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 生日
     */
    private Date birth;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 职业
     */
    private String job;
    /**
     * 个性签名
     */
    private String sign;
    /**
     * 用户来源
     */
    private Integer sourceType;
    /**
     * 积分
     */
    private Integer integration;
    /**
     * 成长值
     */
    private Integer growth;
    /**
     * 启用状态
     */
    private Integer status;
    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 社交账号的uid
     */
    private String uid;
    /**
     *  社交账号登录令牌的过期时间
     */
    private String expiresIn;
    /**
     * 社交账号登录的令牌
     */
    private String accessToken;

}
