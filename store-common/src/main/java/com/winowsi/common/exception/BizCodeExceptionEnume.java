package com.winowsi.common.exception;

/**
 * @Description:
 * 10 通用
 *  001 参数格式校验
 *  002 短信验证码频率太高
 * 11 商品
 * 12 订单
 * 13 购物车
 * 14 物流
 * 15 用户
 * @author Tom
 * @date 2021年10月8日13:24:32
 */
public enum BizCodeExceptionEnume {
    /**
     * UNKNOWN_EXCEPTION
     */
    UNKNOWN_EXCEPTION(10000,"系统未知异常"),
    /**
     * VALID_EXCEPTION
     */
    VALID_EXCEPTION(10001,"参数格式校验失败"),
    /**
     * 短信验证码获取频繁
     */
    SMS_CODE_EXCEPTION(10002,"短信验证码频率太高稍后再试"),
    /**
     * PRODUCT_UP_EXCEPTION
     */
    PRODUCT_UP_EXCEPTION(11000,"商品上架功能异常"),
    /**
     * 用户手机号存在异常
     */
    USER_PHONE_EXIST_EXCEPTION(15001,"用户手机号存在异常"),
    /**
     * 用户名存在异常异常
     */
    User_NAME_EXIST_EXCEPTION(15002,"用户名存在异常异常"),
    /**
     * 账号密码错误
     */
    UserNAME_AND_PASSWORD_EXCEPTION(15004,"账号密码错误");

    private Integer code;
    private String msg;

    BizCodeExceptionEnume(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
