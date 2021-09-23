package com.winowsi.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StoreCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreCouponApplication.class, args);
    }

}
