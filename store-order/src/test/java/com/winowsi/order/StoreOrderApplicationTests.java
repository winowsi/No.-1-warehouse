package com.winowsi.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@Slf4j
@SpringBootTest
class StoreOrderApplicationTests {
    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 创建交换机
     */
    @Test
    void createExchange() {
        amqpAdmin.declareExchange(new DirectExchange("hello_javaExchange",true,false));
        log.info("Exchange[{}]创建成功","hello_javaExchange");
    }

    /**
     * 创建一个队列
     */
    @Test
    void createQueue(){
        amqpAdmin.declareQueue(new Queue("hello_javaQueue",true,false,false));
        log.info("Queue[{}]创建成功","hello_javaQueue");
    }
    /**
     * 创建绑定
     * String destination, [目的地]
     * Binding.DestinationType destinationType, [目的地的类型]
     * String exchange,[交换机]
     * String routingKey,[路由键]
     * @Nullable Map<String, Object> arguments [参数]
     * exchange指定的交换机和 destination 的目的地进行绑定 类型[交换机的类型] routingKey为路由键
     */
    @Test
    void createBinding(){
        amqpAdmin.declareBinding(new Binding("hello_javaQueue",
                Binding.DestinationType.QUEUE,"hello_javaExchange",
                "hello.java",null));
        log.info("Binding[{}]绑定成功","hello_javaBinding");
    }

    /**
     * 发送消息
     * 如果发送的消息是一个object 时必须实现序列化接口
     */
    @Test
    void sendMessage(){
        rabbitTemplate.convertAndSend("hello_javaExchange","hello.java","Hello Word");
        log.info("sendMessage[{}]发送完成","Hello Word");
    }


}
