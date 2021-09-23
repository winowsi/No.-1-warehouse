package com.winowsi.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.winowsi.member.feign.CouponFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.winowsi.member.entity.UmsMemberEntity;
import com.winowsi.member.service.UmsMemberService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.R;



/**
 * 会员
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:31:11
 */
@RestController
@RequestMapping("member/umsmember")
public class UmsMemberController {
    @Autowired
    private UmsMemberService umsMemberService;
    @Autowired
    private CouponFeignService couponFeignService;

    /**
     * @auth zhao yao
     * @return 会员集合and优惠券集合
     * @description: 用于测试远程调用store_coupon微服务的controller接口
     * @date:2021年9月23日09:53:52
     */
    @GetMapping("memberCoupon/List")
    public R memberCouponList(){
        UmsMemberEntity umsMember = new UmsMemberEntity();
        umsMember.setNickname("王五");
        R couponList = couponFeignService.bigCouponList();
        return R.ok().put("umsMember",umsMember).put("coupon",couponList.get("coupon"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = umsMemberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		UmsMemberEntity umsMember = umsMemberService.getById(id);

        return R.ok().put("umsMember", umsMember);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UmsMemberEntity umsMember){
		umsMemberService.save(umsMember);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UmsMemberEntity umsMember){
		umsMemberService.updateById(umsMember);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		umsMemberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
