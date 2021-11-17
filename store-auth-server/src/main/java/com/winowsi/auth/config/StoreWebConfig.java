package com.winowsi.auth.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/11/2 16:58
 */
@Configuration
public class StoreWebConfig implements WebMvcConfigurer {
    /**
     *     视图映射
     * 去掉controller的页面跳转的空方法
     *
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
