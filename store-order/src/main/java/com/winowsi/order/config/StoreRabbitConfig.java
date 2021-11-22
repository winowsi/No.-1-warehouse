package com.winowsi.order.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/22 12:03
 */
@Configuration
public class StoreRabbitConfig {
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct
    private void initConfirmCallback(){
        /**
         * 只要消息没有成功发送的brock服务器就会触发此方法
         * @param correlationData correlation data for the callback. 当前消息的唯一关联数据(这个消息的饿唯一ID)
         * @param ack true for ack, false for nack 是否成功收到消息
         * @param cause An optional cause, for nack, when available, otherwise null. 失败的原因
         */
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause)->{
            System.out.println(correlationData+"\t"+ack+"\t"+cause);
        });
        /**
         * 只要消息没有法送给指定的队列就会触发这个方法
         * Returned message callback.
         * @param message the returned message. 投递失败的消息详细信息
         * @param replyCode the reply code.回复的状态码
         * @param replyText the reply text.会发的文本内容
         * @param exchange the exchange.当时这个消息发送给的那个交换机
         * @param routingKey the routing key.当时这个消息指定的路由键
         */
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey)->{
            System.out.println(message+"\t"+replyCode+"\t"+replyText+"\t"+exchange+"\t"+routingKey);
        });
    }
}
