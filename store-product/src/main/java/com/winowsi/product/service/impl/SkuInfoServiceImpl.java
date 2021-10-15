package com.winowsi.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.product.dao.SkuInfoDao;
import com.winowsi.product.entity.SkuInfoEntity;
import com.winowsi.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    /**
     *
     *
     * sku的 查询
     * @param params key:
     *               catelogId: 0
     *               brandId: 0
     *               min: 0
     *               max: 0
     * @return
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        String key = (String) params.get("key");
        String catelogId = (String) params.get("catelogId");
        String brandId = (String) params.get("brandId");
        String min = (String) params.get("min");
        String max = (String) params.get("max");
        String value = "0";
        QueryWrapper<SkuInfoEntity> skuInfoEntityQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(key)) {
            skuInfoEntityQueryWrapper.and(skuInfoWrapper -> {
                skuInfoWrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }
        if (StringUtils.isNotEmpty(brandId) && !value.equalsIgnoreCase(brandId)) {
            skuInfoEntityQueryWrapper.eq("brand_id", brandId);
        }
        if (StringUtils.isNotEmpty(catelogId) && !value.equalsIgnoreCase(catelogId)) {
            skuInfoEntityQueryWrapper.eq("catalog_id", catelogId);
        }
        if (StringUtils.isNotEmpty(min)) {
            skuInfoEntityQueryWrapper.ge("price", min);
        }
        if (StringUtils.isNotEmpty(max)) {
            BigDecimal bigDecimal = new BigDecimal(max);

            try {
                if (bigDecimal.compareTo(new BigDecimal(value)) == 1) {
                    skuInfoEntityQueryWrapper.le("price", max);
                }
            } catch (Exception e) {
            }

        }


        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                skuInfoEntityQueryWrapper
        );
        return new PageUtils(page);

    }

}