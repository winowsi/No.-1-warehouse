package com.winowsi.cart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * @description: 描述 购物项
 * @author: ZaoYao
 * @time: 2021/11/16 17:25
 */
public class CartItem {
    /**
     * 商品Id
     */
    private Long skuId;
    /**
     * 是否被选中
     */
    private boolean check = true;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片
     */
    private String defaultImage;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 小计
     */
    private BigDecimal totalPrice;
    /**
     * 套餐
     */
    private List<String> skuAttr;

    public CartItem() {
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getTotalPrice() {
    return this.price.multiply(new BigDecimal("" + this.count));
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<String> getSkuAttr() {
        return skuAttr;
    }

    public void setSkuAttr(List<String> skuAttr) {
        this.skuAttr = skuAttr;
    }
}
