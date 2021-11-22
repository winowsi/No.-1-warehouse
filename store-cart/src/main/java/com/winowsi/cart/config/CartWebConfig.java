package com.winowsi.cart.config;

import com.winowsi.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: 描述 设置拦截器和拦截的请求地址
 * @author: ZaoYao
 * @time: 2021/11/17 13:00
 */
@Configuration
public class CartWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
