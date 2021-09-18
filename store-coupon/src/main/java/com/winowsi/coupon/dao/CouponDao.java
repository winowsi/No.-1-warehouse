package com.winowsi.coupon.dao;

import com.winowsi.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:22:40
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
