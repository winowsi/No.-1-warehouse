package com.winowsi.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/15 15:26
 */
@Data
public class SkuItemSaleAttrVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValue;
}
