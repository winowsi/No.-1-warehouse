package com.winowsi.auth.feign;

import com.winowsi.auth.vo.AccountLoginVo;
import com.winowsi.auth.vo.TokenVo;
import com.winowsi.auth.vo.UserRegisterVo;
import com.winowsi.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/4 10:59
 */
@FeignClient("store-member")
public interface MemberFeignService {
    /**
     * @time: 2021年11月4日11:04:47
     * 注册实体远程接口
     *
     */
    @PostMapping("member/member/register")
     R register(@RequestBody UserRegisterVo userRegisterVo);

    @PostMapping("member/member/login")
    R login(@RequestBody AccountLoginVo AccountLoginVo);

    @PostMapping("member/member/aouth2/login")
    R login(@RequestBody TokenVo tokenVo);
}
