package com.winowsi.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;



/**
 * @description:跨域配置
 * @Author:ZaoYao
 * @Time: 2021/9/28 12:32
 */
@Configuration
public class StoreCorsConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //任意请求头
        corsConfiguration.addAllowedHeader("*");
        //任意请求方法
        corsConfiguration.addAllowedMethod("*");
        //任意来源
        corsConfiguration.addAllowedOrigin("*");
        //是否携带cookie信息
        corsConfiguration.setAllowCredentials(true);
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }



}
