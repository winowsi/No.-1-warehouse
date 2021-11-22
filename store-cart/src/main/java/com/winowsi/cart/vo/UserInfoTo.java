package com.winowsi.cart.vo;

import lombok.Data;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/17 11:23
 */
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    private boolean tempUser=false;
}
