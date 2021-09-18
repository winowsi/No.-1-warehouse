package com.winowsi.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.coupon.entity.MemberPriceEntity;

import java.util.Map;

/**
 * 商品会员价格
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:22:40
 */
public interface MemberPriceService extends IService<MemberPriceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

