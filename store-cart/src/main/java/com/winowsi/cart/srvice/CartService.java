package com.winowsi.cart.srvice;

import com.winowsi.cart.vo.Cart;
import com.winowsi.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/17 15:40
 */

public interface CartService {
    /**
     * 将购物项添加到购物车中
     * @param skuId 销售版本Id
     * @param num 数量
     * @return 购物项
     */
    CartItem setToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车中的某个购物项
     */
    CartItem getCatItem(Long skuId);

    /**
     * 获取购物车
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车
     */
    void clearCart(String cartKey);

    /**
     * 选择购物项
     * @param skuId
     * @param check
     */
    void checkItem(Long skuId, Integer check);

    /**
     * @description: 修改购物项数量
     * @param skuId 版本Id
     * @param num 数量
     */
    void changeItemCont(Long skuId, Integer num);

    /**
     * 删除购物项
     * @param skuId
     */
    void deleteItem(Long skuId);
}
