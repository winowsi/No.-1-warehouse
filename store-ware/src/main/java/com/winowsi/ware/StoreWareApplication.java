package com.winowsi.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * description:库存服务
 * @author Tom
 * date:2021年9月23日09:24:37
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.winowsi.ware.feign")
public class StoreWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreWareApplication.class, args);
    }

}
