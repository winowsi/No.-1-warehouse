package com.winowsi.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.winowsi.common.to.SkuReductionTo;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:22:40
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {
    /**
     * 获取分页数据
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存详情
     * @param skuReductionTo
     */
    void saveReduction(SkuReductionTo skuReductionTo);
}

