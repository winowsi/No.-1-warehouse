package com.winowsi.cart.controller;

import com.winowsi.cart.srvice.CartService;
import com.winowsi.cart.vo.Cart;
import com.winowsi.cart.vo.CartItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;


/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/16 15:44
 */
@Controller
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId){
        cartService.deleteItem(skuId);
        return "redirect:http://cart.gulimall.com//cart.html";
    }

    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,@RequestParam("checked") Integer check){
        cartService.checkItem(skuId,check);
        return "redirect:http://cart.gulimall.com//cart.html";
    }

    @GetMapping("/cart.html")
    public String getCartList( Model model) throws ExecutionException, InterruptedException {
       Cart cart= cartService.getCart();
        model.addAttribute("cart",cart);
        return "cartList";
    }

    /**
     *
     * @param skuId
     * @param num
     * @param model
     * RedirectAttributes
     *   model.addFlashAttribute() 模拟session 的方式只能用一次
     *      * model.addAttribute("skuId",skuId)问号拼串?skuId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping({"/addToCart","/countItem"})
    public String setToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num, RedirectAttributes model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        boolean countItem = request.getRequestURI().contains("countItem");
        if (!countItem){
            cartService.setToCart(skuId,num);
            model.addAttribute("skuId",skuId);
            return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
        }else {
            cartService.changeItemCont(skuId,num);
            return "redirect:http://cart.gulimall.com//cart.html";
        }

    }

    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccess(@RequestParam("skuId") Long skuId,Model model){
        //根据skuId查询购物车中的购物项
       CartItem cartItem= cartService.getCatItem(skuId);
       model.addAttribute("cartItem",cartItem);
        return "success";
    }


}
