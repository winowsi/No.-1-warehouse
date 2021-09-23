package com.winowsi.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import com.winowsi.coupon.entity.CouponEntity;
import com.winowsi.coupon.service.CouponService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.R;



/**
 * 优惠券信息
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:22:40
 */
@RefreshScope
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;
    @Value("${coupon.user.name}")
    private String name;
    @Value("${coupon.user.age}")
    private String age;
    @GetMapping("/get/user")
    public R getUser(){
        return R.ok().put("name",name).put("age",age);
    }


    /**
     * description 测试feign的远程调用
     * @return
     */
    @GetMapping("/bigCouponList")
    public R bigCouponList(){
        CouponEntity coupon=new CouponEntity();
        coupon.setCouponName("满100减1元");
        return R.ok().put("coupon",Arrays.asList(coupon));
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CouponEntity coupon){
		couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CouponEntity coupon){
		couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
