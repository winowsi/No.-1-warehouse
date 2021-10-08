package com.winowsi.common.exception;

/**
 * @Description:
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
    VALID_EXCEPTION(10001,"参数格式校验失败");
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
