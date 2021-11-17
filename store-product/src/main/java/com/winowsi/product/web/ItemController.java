package com.winowsi.product.web;

import com.winowsi.product.service.SkuInfoService;
import com.winowsi.product.vo.SkuItemVo;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.jws.WebParam;
import java.util.concurrent.ExecutionException;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/15 10:16
 */
@Controller
public class ItemController {
    private final SkuInfoService skuInfoService;

    public ItemController(SkuInfoService skuInfoService) {
        this.skuInfoService = skuInfoService;
    }

    @GetMapping("/{skuId}.html")
    public String getSkuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
       SkuItemVo itemVo=skuInfoService.item(skuId);
       model.addAttribute("item",itemVo);
        return "item";
    }
}

