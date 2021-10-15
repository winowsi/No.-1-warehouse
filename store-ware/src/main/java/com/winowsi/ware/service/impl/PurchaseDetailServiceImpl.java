package com.winowsi.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.ware.dao.PurchaseDetailDao;
import com.winowsi.ware.entity.PurchaseDetailEntity;
import com.winowsi.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    /**
     * @param params wareId: status: key:
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String wareId = (String) params.get("wareId");
        QueryWrapper<PurchaseDetailEntity> purchaseDetailEntityQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(key)) {
            purchaseDetailEntityQueryWrapper.and(purchaseDetailQueryWrapper -> {
                purchaseDetailQueryWrapper.eq("id", key).or().eq("purchase_id", key);
            });
        }
        if (StringUtils.isNotEmpty(wareId)) {
            purchaseDetailEntityQueryWrapper.eq("ware_id", wareId);

        }
        if (StringUtils.isNotEmpty(status)) {
            purchaseDetailEntityQueryWrapper.eq("status", status);

        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                purchaseDetailEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetaliByPurchas(Long id) {
        return this.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));
    }

}