package com.winowsi.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.winowsi.auth.feign.MemberFeignService;
import com.winowsi.auth.feign.SmsSendCodeFeignService;
import com.winowsi.auth.vo.AccountLoginVo;
import com.winowsi.auth.vo.UserRegisterVo;
import com.winowsi.common.constant.AuthServerConstant;
import com.winowsi.common.exception.BizCodeExceptionEnume;
import com.winowsi.common.utils.R;
import com.winowsi.common.vo.MemberVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description:
 * @Author: ZaoYao
 * @Time: 2021/11/3 13:12
 */
@Controller
public class LoginController {
    private final MemberFeignService memberFeignService;
    private final SmsSendCodeFeignService smsSendCodeFeignService;
    private final StringRedisTemplate stringRedisTemplate;
    @Autowired
    public LoginController(SmsSendCodeFeignService smsSendCodeFeignService, StringRedisTemplate stringRedisTemplate, MemberFeignService memberFeignService) {
        this.smsSendCodeFeignService = smsSendCodeFeignService;
        this.stringRedisTemplate=stringRedisTemplate;
        this.memberFeignService = memberFeignService;
    }

    @GetMapping("/sms/sendCode")
    @ResponseBody
    public R getSentSmsCode(@RequestParam("phone")  String phone){
        // TODO  防止重复刷码
        String s = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
       if (StringUtils.isNotEmpty(s)){
           long lod = Long.parseLong(s.split(AuthServerConstant.SMS_CODE_PREFIX)[1]);
           long cont=60000L;
           if (System.currentTimeMillis()-lod<cont){
               return R.error(BizCodeExceptionEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeExceptionEnume.SMS_CODE_EXCEPTION.getMsg());
           }
       }
       //验证码+系统时间
        String substringCode = UUID.randomUUID().toString().substring(0, 5)+AuthServerConstant.SMS_CODE_PREFIX+System.currentTimeMillis();
       //短信验证码
        String code = substringCode.split(AuthServerConstant.SMS_CODE_PREFIX)[0];
        // 验证码校验
        //key=sms:code:phone  value=substringCode
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone,substringCode,5, TimeUnit.MINUTES);
        //发送短信
        smsSendCodeFeignService.sendSmsCode(phone,code);
        return R.ok();
    }
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo userRegisterVo, BindingResult result, RedirectAttributes model){
        //jsr数据校验返回的结果
        if (result.hasErrors()){
            // 获取每个属性的错误消息 属性为k 错误为v 通过map进行封装 添加个model
            Map<String, String> collect = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fieldError->{
                String defaultMessage = fieldError.getDefaultMessage();
                // 断言如果不是期望的值直接抛出异常
                Assert.notNull(defaultMessage,"defaultMessage = null");
                return defaultMessage;
            }));
            model.addFlashAttribute("error",collect);
            //校验出错转发给注册页
            return "redirect://auth.gulimall.com/reg.html";
        }
        //调用远程服务进行注册
        String code = userRegisterVo.getCode();
        String s = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegisterVo.getPhone());
        if (StringUtils.isNotEmpty(s)){
            if (code.equalsIgnoreCase(s.split(AuthServerConstant.SMS_CODE_PREFIX)[0])){
                //删除验证码 :令牌机制
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX+userRegisterVo.getPhone());
                //远程调用 注册会员 发送json数据
                R register = memberFeignService.register(userRegisterVo);
                if (register.grtCode()==0){
                    //注册成功返回到首页 登录页
                    return "redirect://auth.gulimall.com/login.html";
                }else {
                    Map<String ,String>errorMap=new HashMap<>(16);
                    errorMap.put("msg",register.getData("msg",new TypeReference<String>(){}));
                    model.addFlashAttribute("error",errorMap);
                    return "redirect://auth.gulimall.com/reg.html";
                }

            }else {
                Map<String ,String>errorMap=new HashMap<>(16);
                errorMap.put("code","验证码错误");
                model.addFlashAttribute("error",errorMap);
                return "redirect://auth.gulimall.com/reg.html";
            }

        }else {
            Map<String ,String>errorMap=new HashMap<>(16);
            errorMap.put("code","验证码错误");
            model.addFlashAttribute("error",errorMap);
            return "redirect://auth.gulimall.com/reg.html";
        }
    }
    @PostMapping("/login")
    public  String login(AccountLoginVo accountLoginVo, RedirectAttributes redirectAttributes, HttpSession session){
        R login = memberFeignService.login(accountLoginVo);
        if (login.grtCode()==0) {
            //TODO 数据回显
            MemberVo member = login.getData("member", new TypeReference<MemberVo>() {});

            session.setAttribute("loginUser",member);
            return "redirect://gulimall.com/";
        }else {
            Map<String ,String>errorMap=new HashMap<>(16);
            errorMap.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("error",errorMap);
            return "redirect://auth.gulimall.com/login.html";
        }


    }
   



}
