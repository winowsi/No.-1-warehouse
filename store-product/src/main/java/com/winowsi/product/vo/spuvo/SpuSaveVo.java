package com.winowsi.product.vo.spuvo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tom
 * @date
 */
@Data
public class SpuSaveVo {

    /**
     * 商品名字
     */
    private String spuName;
    /**
     * 商品描述
     */
    private String spuDescription;
    /**
     * 商品分类Id
     */
    private Long catalogId;
    /**
     * 品牌Id
     */
    private Long brandId;
    /**
     * 重量
     */
    private BigDecimal weight;
    /**
     * 发布状态
     */
    private int publishStatus;
    /**
     * 描述
     */
    private List<String> decript;
    /**
     * 图片
     */
    private List<String> images;
    /**
     * 积分
     */
    private Bounds bounds;
    /**
     * 基本属性
     */
    private List<BaseAttrs> baseAttrs;
    /**
     * sku
     */
    private List<Skus> skus;


}