package com.winowsi.cart.vo;



import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 描述 购物车
 * @author: ZaoYao
 * @time: 2021/11/16 17:25
 */

public class Cart {
    /**
     * 购物项
     */
    private List<CartItem> items;
    /**
     * 购物项计数(商品数量)
     */
    private Integer countNumber;
    /**
     * 商品的类型(不同的商品数量)
     */
    private Integer countType;
    /**
     * 合计总价
     */
    private BigDecimal totalAmount;
    /**
     * 优惠金额
     */
    private BigDecimal reduce;
}
