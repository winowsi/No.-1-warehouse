package com.winowsi.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.winowsi.common.exception.BizCodeExceptionEnume;
import com.winowsi.member.exception.PhoneExsitException;
import com.winowsi.member.exception.UserNameExsitException;
import com.winowsi.member.feign.CouponFeignService;
import com.winowsi.member.vo.MemberLoginVo;
import com.winowsi.member.vo.MemberRegisterVo;
import com.winowsi.member.vo.TokenVo;
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
    private final MemberService memberService;
    private final CouponFeignService couponFeignService;
    @Autowired
    public MemberController(CouponFeignService couponFeignService, MemberService memberService) {
        this.couponFeignService = couponFeignService;
        this.memberService = memberService;
    }

    /**
     * @author: ZaoYao
     * @return 会员集合and优惠券集合
     * @description: 用于测试远程调用store_coupon微服务的controller接口
     * @time: :2021年9月23日09:53:52
     */
    @GetMapping("memberCoupon/List")
    public R memberCouponList(){
        MemberEntity umsMember = new MemberEntity();
        umsMember.setNickname("王五");
        R couponList = couponFeignService.bigCouponList();
        return R.ok().put("umsMember",umsMember).put("coupon",couponList.get("coupon"));
    }

    /**
     * 登录
     * @param memberLoginVo
     * @return
     */
    @PostMapping("/login")
    public  R login(@RequestBody MemberLoginVo memberLoginVo){
        MemberEntity member=memberService.login(memberLoginVo);
        if(member!=null){
            return R.ok().put("member",member);
        }else {
            return R.error(BizCodeExceptionEnume.UserNAME_AND_PASSWORD_EXCEPTION.getCode(),BizCodeExceptionEnume.UserNAME_AND_PASSWORD_EXCEPTION.getMsg());
        }
    }
    @PostMapping("/aouth2/login")
    public  R login(@RequestBody TokenVo tokenVo){
        MemberEntity member=memberService.login(tokenVo);
        if(member!=null){
            return R.ok().put("member",member);
        }else {
            return R.error(BizCodeExceptionEnume.UserNAME_AND_PASSWORD_EXCEPTION.getCode(),BizCodeExceptionEnume.UserNAME_AND_PASSWORD_EXCEPTION.getMsg());
        }
    }

    /**
     * 注册
     * @param memberRegisterVo
     * @return
     */

    @PostMapping("/register")
    public  R register(@RequestBody MemberRegisterVo memberRegisterVo){

        try {
            memberService.register(memberRegisterVo);
        } catch (PhoneExsitException e) {
           return R.error(BizCodeExceptionEnume.USER_PHONE_EXIST_EXCEPTION.getCode(),BizCodeExceptionEnume.USER_PHONE_EXIST_EXCEPTION.getMsg());
        }
        catch (UserNameExsitException e) {
            return R.error(BizCodeExceptionEnume.User_NAME_EXIST_EXCEPTION.getCode(),BizCodeExceptionEnume.User_NAME_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
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
