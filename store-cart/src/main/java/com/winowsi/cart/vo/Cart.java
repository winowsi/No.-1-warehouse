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
    private BigDecimal reduce=new BigDecimal("0");

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNumber() {
        Integer count = 0;
        if (null!=items&&items.size() > 0) {
            for (CartItem item : items) {
                count += item.getCount();
            }
        }
        return count;
    }


    public Integer getCountType() {
        int count = 0;
        if (null!=items&&items.size() > 0) {
            for (CartItem item : items) {
                count +=1;
            }
        }
        return count;
    }



    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        if (null!=items&&items.size() > 0) {
            for (CartItem item : items) {
                if (item.isCheck()){
                    amount = amount.add(item.getTotalPrice());
                }
            }
            amount= amount.subtract(getReduce());
        }

        return amount;
    }



    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
