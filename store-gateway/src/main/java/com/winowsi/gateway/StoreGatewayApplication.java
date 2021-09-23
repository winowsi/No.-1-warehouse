package com.winowsi.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/9/23 11:00
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class StoreGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreGatewayApplication.class,args);
    }
}
