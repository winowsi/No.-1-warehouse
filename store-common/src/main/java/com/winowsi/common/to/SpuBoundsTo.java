package com.winowsi.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/14 15:34
 */
@Data
public class SpuBoundsTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
