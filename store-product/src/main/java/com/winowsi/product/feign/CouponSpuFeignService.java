package com.winowsi.product.feign;

import com.winowsi.common.to.SkuReductionTo;
import com.winowsi.common.to.SpuBoundsTo;
import com.winowsi.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/14 15:21
 */
@FeignClient("store-coupon")
public interface CouponSpuFeignService {
    /**
     * 保存积分
     * @description: 保存积分
     * @param spuBoundsTo
     * @return
     */
    @PostMapping("coupon/spubounds/save")
    R saveBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    /**
     * 保存满减优惠折扣
     * @param skuReductionTo
     * @return
     */
    @PostMapping("coupon/skufullreduction/save/info")
    R saveReduction(@RequestBody SkuReductionTo skuReductionTo);
}
