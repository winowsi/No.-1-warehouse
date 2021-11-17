package com.winowsi.auth.feign;

import com.winowsi.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Tom
 */
@FeignClient("story-thirdparty")
public interface SmsSendCodeFeignService {

    /**
     *  获取验证码
     * @param mobile 电话号码
     * @param code 验证码
     * @return  R
     *
     */
    @GetMapping("/sms/sendcode")
    R sendSmsCode(@RequestParam("phone") String mobile , @RequestParam("code") String code );
}
