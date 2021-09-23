package com.winowsi.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * description: 会员服务
 * @author Tom
 * @date 2021年9月23日09:17:25
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.winowsi.member.feign")
public class StoreMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreMemberApplication.class, args);
    }

}
