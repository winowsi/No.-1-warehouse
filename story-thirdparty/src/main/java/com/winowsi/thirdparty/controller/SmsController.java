package com.winowsi.thirdparty.controller;

import com.winowsi.common.utils.R;
import com.winowsi.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/11/3 13:03
 */

@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    private SmsComponent smsComponent;

    /**
     * @description: 提供给其他服务调用的的接口
     * @param mobile 电话号码
     * @param code 验证码
     * @return R
     */
    @GetMapping("/sendcode")
    public R sendSmsCode(@RequestParam("phone") String mobile ,@RequestParam("code") String code ){
        smsComponent.sendSmsCode(mobile,code);
        return R.ok();
    }
}
