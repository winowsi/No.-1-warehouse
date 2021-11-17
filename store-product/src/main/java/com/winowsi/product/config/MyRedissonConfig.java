package com.winowsi.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/11/1 10:14
 */
@Configuration
public class MyRedissonConfig {
    /**
     * redisson的单节点模式 ssh连接
     * destroyMethod ="shutdown" 销毁方法
     *
     * @return redissonClient
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.56.128:6379");
        return Redisson.create(config);
    }

}
