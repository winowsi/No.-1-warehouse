package com.winowsi.cart.srvice.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.winowsi.cart.feign.ProductFeignService;
import com.winowsi.cart.interceptor.CartInterceptor;
import com.winowsi.cart.srvice.CartService;
import com.winowsi.cart.vo.Cart;
import com.winowsi.cart.vo.CartItem;
import com.winowsi.cart.vo.SkuInfoVo;
import com.winowsi.cart.vo.UserInfoTo;
import com.winowsi.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/17 15:40
 */
@Service
public class CartServiceImpl implements CartService {
    private final ProductFeignService productFeignService;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final StringRedisTemplate redisTemplate;
    private final String  CART_PREFIX="gulimall:cart:";

    public CartServiceImpl(ProductFeignService productFeignService, ThreadPoolExecutor threadPoolExecutor, StringRedisTemplate redisTemplate) {
        this.productFeignService = productFeignService;
        this.threadPoolExecutor = threadPoolExecutor;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 将购物项添加到购物车
     * @param skuId 销售版本Id
     * @param num 数量
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public CartItem setToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartRedisBoundHashOps = getCartRedisBoundHashOps();

        String res = (String) cartRedisBoundHashOps.get(skuId.toString());
        CartItem cartItem = new CartItem();
        //商品存在时则数量叠加
        if (StringUtils.isNotEmpty(res)){
            CartItem seekCartItem = JSON.parseObject(res, CartItem.class);
            cartItem.setCount(seekCartItem.getCount()+num);
            String s = JSON.toJSONString(cartItem);
            cartRedisBoundHashOps.put(skuId.toString(),s);
            return cartItem;
        }

        CompletableFuture<Void> skuInfoFuture = CompletableFuture.runAsync(() -> {
            //远程调用商品服务
            R info = productFeignService.info(skuId);
            SkuInfoVo skuInfo = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {
            });
            if (null != skuInfo) {
                cartItem.setSkuId(skuId);
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setDefaultImage(skuInfo.getSkuDefaultImg());
                cartItem.setPrice(skuInfo.getPrice());
                cartItem.setTitle(skuInfo.getSkuTitle());

            }
        }, threadPoolExecutor);
        CompletableFuture<Void> attrFuture = CompletableFuture.runAsync(() -> {
            //远程调用商品服务根据skuID查到销售组合
            List<String> skuSaleAttrValue = productFeignService.getSkuSaleAttrValue(skuId);
            cartItem.setSkuAttr(skuSaleAttrValue);
        }, threadPoolExecutor);

        CompletableFuture.allOf(skuInfoFuture,attrFuture).get();
        String s = JSON.toJSONString(cartItem);
        cartRedisBoundHashOps.put(skuId.toString(),s);

        return cartItem;
    }

    /**
     * 获取单个购物项
     * @param skuId
     * @return
     */
    @Override
    public CartItem getCatItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartRedisBoundHashOps = getCartRedisBoundHashOps();
        String o = (String) cartRedisBoundHashOps.get(skuId.toString());
        return JSON.parseObject(o, CartItem.class);
    }

    /**
     * 获取购车
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        StringBuilder stringBuilder = new StringBuilder();
        Cart cart = new Cart();
        if (null!=userInfoTo.getUserId()){
            //用户已登录
            stringBuilder.append(CART_PREFIX).append(userInfoTo.getUserId());
            //临时购物车的key
            String tempCartItems = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> cartItem = getCartItem(tempCartItems);
            //临时购物车不为空合并购物车
            if (cartItem!=null){
                for (CartItem item : cartItem) {
                    setToCart(item.getSkuId(),item.getCount());
                }
                //合并完了清空
                clearCart(tempCartItems);
            }
            //获取登录后的购物车,包含合并后的临时购物车的数据 的购物车总数据
            List<CartItem> cartItemList = getCartItem(stringBuilder.toString());
            cart.setItems(cartItemList);

        }else {
            //用户没登录
            stringBuilder.append(CART_PREFIX).append(userInfoTo.getUserKey());
            List<CartItem> cartItem = getCartItem(stringBuilder.toString());
            cart.setItems(cartItem);
        }
        return cart;
    }

    /**
     * 清空购物车
     * @param cartKey
     */
    @Override
    public void clearCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    /**
     * 购物项 选中or不选中
     * @param skuId
     * @param check
     */
    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> cartRedisBoundHashOps = getCartRedisBoundHashOps();
        CartItem catItem = getCatItem(skuId);
        catItem.setCheck(check==1?true:false);
        String s = JSON.toJSONString(catItem);
        cartRedisBoundHashOps.put(skuId.toString(),s);

    }

    /**
     *
     * @param skuId 版本Id
     * @param num 数量
     */
    @Override
    public void changeItemCont(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> cartRedisBoundHashOps = getCartRedisBoundHashOps();
        CartItem catItem = getCatItem(skuId);
        catItem.setCount(num);
        cartRedisBoundHashOps.put(skuId.toString(),JSON.toJSONString(catItem));
    }

    /**
     * 删除购物项
     * @param skuId
     */
    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartRedisBoundHashOps = getCartRedisBoundHashOps();
        cartRedisBoundHashOps.delete(skuId.toString());
    }

    /**
     * 获取所有购物项
     * @param cartKey 检索前缀key
     * @return 购物项的list集合
     */
    private List<CartItem> getCartItem(String cartKey) {
        BoundHashOperations<String, Object, Object> boundHashOperations = redisTemplate.boundHashOps(cartKey);
        List<Object> values = boundHashOperations.values();
        if (values!=null&&values.size()>0){
            return  values.stream().map(o -> {
                String str= (String) o;
                return JSON.parseObject(str, CartItem.class);
            }).collect(Collectors.toList());

        }
        return null;
    }

    /**
     *
     * 登录和未登录的key前缀设置
     */
    private  BoundHashOperations<String, Object, Object> getCartRedisBoundHashOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        StringBuilder buffer=new StringBuilder();
        if (null!=userInfoTo.getUserId()){
            buffer.append(CART_PREFIX).append(userInfoTo.getUserId());
        }else {
            buffer.append(CART_PREFIX).append(userInfoTo.getUserKey());
        }
        //哈希数据结构
       return redisTemplate.boundHashOps(buffer.toString());
    }
}
