package com.winowsi.ware.vo;

import lombok.Data;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/15 17:10
 */
@Data
public class ItemDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
