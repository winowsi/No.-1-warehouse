package com.winowsi.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.winowsi.member.feign.CouponFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.winowsi.member.entity.MemberEntity;
import com.winowsi.member.service.MemberService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.R;



/**
 * 会员
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-10-13 10:12:43
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

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
        MemberEntity umsMember = new MemberEntity();
        umsMember.setNickname("王五");
        R couponList = couponFeignService.bigCouponList();
        return R.ok().put("umsMember",umsMember).put("coupon",couponList.get("coupon"));
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
