package com.winowsi.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/15 15:33
 */
@Data
public class MergeVo {
    /**
     * 整单Id
     */
    private Long purchaseId;
    /**
     * 合并项集合
     */
    private List<Long> items;
}
