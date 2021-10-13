package com.winowsi.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * description:商品服务
 * @author Tom
 * @date 2021年9月23日09:22:07
 */
@SpringBootApplication
@EnableDiscoveryClient
//@MapperScan("com.winowsi.product.dao")
public class StoreProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreProductApplication.class, args);
    }

}
