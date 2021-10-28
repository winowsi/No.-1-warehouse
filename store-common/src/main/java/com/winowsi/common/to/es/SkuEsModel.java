package com.winowsi.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/21 10:26
 */
@Data
public class SkuEsModel {
    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;
    /**
     * 销量
     */
    private Long saleCount;
    /**
     * 是否拥有库存
     */
    private boolean hasStock;
    /**
     * 热度
     */
    private Long hotScore;

    private Long brandId;

    private String brandName;

    private String brandImg;

    private Long catalogId;

    private String catalogName;

    private List<Attrs> attrs;

    @Data
    public static class Attrs{
        private  Long attrId;

        private String attrName;

        private String attrValue;
    }
}
