package com.winowsi.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/14 15:55
 */
@Data
public class SkuReductionTo {

    private  Long skuId;
    private int fullCount;
    /**
     * 折扣
     */
    private BigDecimal discount;
    private int countStatus;
    /**
     * 满减价格
     */
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
