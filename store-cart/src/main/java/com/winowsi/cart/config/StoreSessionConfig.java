package com.winowsi.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/17 10:38
 */
@EnableRedisHttpSession
@Configuration
public class StoreSessionConfig {
    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookieName("STORESESSION");
        //tomcat版本决定加不加点
        defaultCookieSerializer.setDomainName("gulimall.com");
        defaultCookieSerializer.setCookiePath("/");
        return defaultCookieSerializer;
    }

    /**
     *
     * Redis json序列化
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }
}
