package com.winowsi.common.to;

import lombok.Data;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/21 13:44
 */
@Data
public class SkuHasStockTo {
    private Long skuId;

    private boolean hasStock;
}
