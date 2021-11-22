package com.winowsi.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Tom
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class StoreCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreCartApplication.class, args);
    }

}
