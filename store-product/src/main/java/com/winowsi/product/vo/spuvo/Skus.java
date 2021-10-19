/**
  * Copyright 2021 json.cn 
  */
package com.winowsi.product.vo.spuvo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

/**
 * Auto-generated: 2021-10-14 9:55:46
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class Skus {

    /**
     * sku名称
     */
    private String skuName;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 标题
     */
    private String skuTitle;
    /**
     * 副标题
     */
    private String skuSubtitle;

    private List<Images> images;
    private List<String> descar;
    private List<Attr> attr;
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