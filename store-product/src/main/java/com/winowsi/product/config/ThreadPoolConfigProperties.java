package com.winowsi.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/16 10:49
 */
@ConfigurationProperties(prefix = "store.thread")
@Component
@Data
public class ThreadPoolConfigProperties {
    /**
     * 核心线程大小
     */
    private Integer coreSize;
    /**
     * 最大线程大小
     */
    private Integer maxSize;
    /**
     * 空闲线程销毁时间
     */
    private  Integer keepAliveTime;
}
