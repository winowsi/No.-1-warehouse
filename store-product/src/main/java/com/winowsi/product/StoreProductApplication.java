package com.winowsi.product;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * description:商品服务
 * @author Tom
 * @date 2021年9月23日09:22:07
 */

@EnableRedisHttpSession
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients(basePackages = "com.winowsi.product.feign")
public class StoreProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreProductApplication.class, args);
    }

}
