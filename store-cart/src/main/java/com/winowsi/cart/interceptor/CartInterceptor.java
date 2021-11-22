package com.winowsi.cart.interceptor;

import com.winowsi.cart.constant.CartConstant;
import com.winowsi.cart.vo.UserInfoTo;
import com.winowsi.common.constant.AuthServerConstant;
import com.winowsi.common.vo.MemberVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @description: 描述 拦截器
 * @author: ZaoYao
 * @time: 2021/11/17 11:17
 */

public class CartInterceptor implements HandlerInterceptor {
    public static  ThreadLocal<UserInfoTo> threadLocal =new ThreadLocal<>();

    /**
     * 业务执行之前
     *  判断cookie 中是否存在 CartConstant.TEMP_USER_COOKIE_NAME
     *  如果有
     *      userInfoTo.setUserKey(cookie.getValue());
     *  如果没有
     *      String s = UUID.randomUUID().toString();
     *      userInfoTo.setUserKey(s);
     *
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();
        MemberVo member = (MemberVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (null != member) {
            userInfoTo.setUserId(member.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals(CartConstant.TEMP_USER_COOKIE_NAME)) {
                    userInfoTo.setUserKey(cookie.getValue());
                    //存在临时用户
                    userInfoTo.setTempUser(true);
                }
            }
        }
        //如果没有临时用户创建一个临时用户
        if (StringUtils.isEmpty(userInfoTo.getUserKey())){
            String s = UUID.randomUUID().toString();
            userInfoTo.setUserKey(s);
        }
        //执行目标方法之前
        threadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 业务执行之后
     * 向浏览器响应自定义的 CartConstant.TEMP_USER_COOKIE_NAME 的cookie
     * userInfoTo.isTempUser() 判断是否已经存在对应的cookie 默认是false
     * 有就不用再设置
     *  没有就设置自定义cookie
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();
        userInfoTo.isTempUser();
        if (!userInfoTo.isTempUser()){
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("gulimall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
        threadLocal.remove();
    }
}
