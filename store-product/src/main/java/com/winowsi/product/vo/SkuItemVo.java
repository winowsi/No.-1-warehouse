package com.winowsi.product.vo;

import com.winowsi.product.entity.SkuImagesEntity;
import com.winowsi.product.entity.SkuInfoEntity;
import com.winowsi.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/15 11:36
 */
@Data
public class SkuItemVo {
    /**
     * sku的基本信息
     */
    private SkuInfoEntity skuInfo;
    /**
     *有货无货
     */
    boolean hasStock=true;
    /**
     * sku的图片信息
     */
    private List<SkuImagesEntity> skuImages;
    /**
     * sku的销售属性
     */
    private List<SkuItemSaleAttrVo> saleAttr;
    /**
     * spu的介绍
     */
    private SpuInfoDescEntity desc;

    /**
     * 获取sku的规格参数
     */
    private List<SpuItemAttrGroupVo> groupAttrs;

}
