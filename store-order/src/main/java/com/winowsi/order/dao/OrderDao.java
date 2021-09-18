package com.winowsi.order.dao;

import com.winowsi.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:44:52
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
