package com.winowsi.product.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/21 13:44
 */
@Data
@NoArgsConstructor
public class SkuHasStockVo {
    private Long skuId;

    private boolean hasStock;
}
