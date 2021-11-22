package com.winowsi.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbitmq.client.Channel;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;
import com.winowsi.order.dao.OrderItemDao;
import com.winowsi.order.entity.OrderItemEntity;
import com.winowsi.order.service.OrderItemService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;


/**
 * @author Tom
 */
@RabbitListener(queues = {"hello_javaQueue"})
@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    /**
     *
     * @param message 原生消息 消息头+消息体
     * @param value <T>发送的消息类型
     * @param channel 当前传输信息的通道
     */
    @RabbitHandler
    public void recieveMessahe(Message message, String value, Channel channel){
        System.out.println(value);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //确认收到消息
            channel.basicAck(deliveryTag,false);
            //拒绝消息,将消息重新入队
//            channel.basicNack(deliveryTag,false,true);
        } catch (IOException e) {
            //网络中断
            e.printStackTrace();
        }
    }


}