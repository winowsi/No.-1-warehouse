package com.winowsi.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.winowsi.auth.feign.MemberFeignService;
import com.winowsi.auth.utils.HttpUtils;
import com.winowsi.auth.vo.TokenVo;
import com.winowsi.common.constant.StatusCode;
import com.winowsi.common.utils.R;
import com.winowsi.common.vo.MemberVo;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 描述OAuth2.0
 * @author: ZaoYao
 * @time: 2021/11/11 15:36
 */
@Controller
public class OauthController {
    private final MemberFeignService memberFeignService;

    public OauthController(MemberFeignService memberFeignService) {
        this.memberFeignService = memberFeignService;
    }

    @GetMapping("/oauth2.0/weibo/success")
    public String weiBo(@RequestParam("code") String code, HttpSession session) throws Exception {
        Map<String, String> map = new HashMap<>(16);
        map.put("client_id", "3219899811");
        map.put("client_secret", "053b1c46e8c5c5b31c5a0861620dda1e");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://auth.gulimall.com/oauth2.0/weibo/success");
        map.put("code", code);
        HttpResponse tokenResponse = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", null, null, map);
        if (tokenResponse.getStatusLine().getStatusCode() == StatusCode.SUCCESS_CODE) {
            String s = EntityUtils.toString(tokenResponse.getEntity());
            TokenVo tokenVo = JSON.parseObject(s, TokenVo.class);
//            新用户自动注册 老用户直接登录 远程调用
            R r = memberFeignService.login(tokenVo);
            if (r.grtCode() == 0) {
                MemberVo member = r.getData("member", new TypeReference<MemberVo>() {});
                session.setAttribute("loginUser",member);
                return "redirect:http://gulimall.com";
            } else {
                return "redirect:http://auth.gulimall.com/login.html";
            }

        } else {
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }
}
