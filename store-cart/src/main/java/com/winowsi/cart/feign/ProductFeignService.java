package com.winowsi.cart.feign;

import com.winowsi.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/17 16:10
 */
@FeignClient("store-product")
public interface ProductFeignService {
    @RequestMapping("product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

    @GetMapping("product/skusaleattrvalue/stringList/{skuId}")
    List<String> getSkuSaleAttrValue(@RequestParam("skuId")Long skuId);
}
