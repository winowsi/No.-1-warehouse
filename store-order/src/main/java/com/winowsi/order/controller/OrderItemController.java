package com.winowsi.order.controller;

import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.R;
import com.winowsi.order.entity.OrderItemEntity;
import com.winowsi.order.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;


/**
 * 订单项信息
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:44:52
 */
@Slf4j
@RestController
@RequestMapping("order/orderitem")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orderItemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		OrderItemEntity orderItem = orderItemService.getById(id);

        return R.ok().put("orderItem", orderItem);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody OrderItemEntity orderItem){
		orderItemService.save(orderItem);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody OrderItemEntity orderItem){
		orderItemService.updateById(orderItem);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		orderItemService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 发送消息
     */
    @GetMapping("/send/{routingKey}")
    public  String sendMessing(@PathVariable(value = "routingKey") String routingKey){
        rabbitTemplate.convertAndSend("hello_javaExchange",routingKey,"Hello Word",new CorrelationData(UUID.randomUUID().toString()));
        log.info("sendMessage[{}]发送完成","Hello Word");
        return "ok";
    }

}
