package com.winowsi.member.feign;

import com.winowsi.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Tom
 */
@FeignClient("store-coupon")
public interface CouponFeignService {

    /**
     * description 测试feign的远程调用
     * @return
     */
    @GetMapping("coupon/coupon/bigCouponList")
    public R bigCouponList();
}
