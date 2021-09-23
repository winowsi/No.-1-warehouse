package com.winowsi.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * description:订单服务
 * @author Tom
 * @date 2021年9月23日09:19:18
 */
@SpringBootApplication
@EnableDiscoveryClient
public class StoreOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreOrderApplication.class, args);
    }

}
