package com.winowsi.ware.feign;

import com.winowsi.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/18 10:31
 */
@FeignClient("store-product")
public interface ProductFeignService {
    /**
     * sku信息
     * @param skuId id
     * @return
     */
    @RequestMapping("product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
