package com.winowsi.search.feign;

import com.winowsi.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/9 17:29
 */
@FeignClient("store-product")
public interface ProductFeignService {
    /**
     * 信息
     * @param attrId 属性id
     * @return  属性信息
     */
    @RequestMapping("product/attr/info/{attrId}")
    R info(@PathVariable("attrId") Long attrId);

    /**
     * 品牌
     * @param brandId 查出品牌信息
     * @return 品牌信息集合
     */
    @RequestMapping("product/brand/infos")
    R info(@RequestParam("brandIds") List<Long> brandId);
}
