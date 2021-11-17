package com.winowsi.product.service.impl;

import com.winowsi.product.entity.SkuImagesEntity;
import com.winowsi.product.entity.SpuInfoDescEntity;
import com.winowsi.product.service.*;
import com.winowsi.product.vo.SkuItemSaleAttrVo;
import com.winowsi.product.vo.SkuItemVo;
import com.winowsi.product.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.product.dao.SkuInfoDao;
import com.winowsi.product.entity.SkuInfoEntity;


/**
 * @author Tom
 */
@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    private final SkuImagesService skuImagesService;
    private final SpuInfoDescService spuInfoDescService;
    private final AttrGroupService attrGroupService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    private final ThreadPoolExecutor threadPoolExecutor;

    public SkuInfoServiceImpl(SkuImagesService service, SpuInfoDescService spuInfoDescService, AttrGroupService attrGroupService, SkuSaleAttrValueService skuSaleAttrValueService, ThreadPoolExecutor threadPoolExecutor) {
        this.skuImagesService = service;
        this.spuInfoDescService = spuInfoDescService;
        this.attrGroupService = attrGroupService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
        this.threadPoolExecutor = threadPoolExecutor;
    }

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
     * sku的 查询
     *
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

    @Override
    public List<SkuInfoEntity> getListBySpuId(Long spuId) {
        return this.baseMapper.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
    }

    /***
     * 异步编排()
     * TODO 复习下 2021年11月16日12:26:58
     * @param skuId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */

    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo itemVo = new SkuItemVo();

        CompletableFuture<SkuInfoEntity> skuInfoEntityCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //sku基本信息的获取
            SkuInfoEntity skuInfo = getById(skuId);
            itemVo.setSkuInfo(skuInfo);
            return skuInfo;
        }, threadPoolExecutor);

        CompletableFuture<Void> saleAttrFuture = skuInfoEntityCompletableFuture.thenAcceptAsync(res -> {
            //获取spu的销售属性组合
            List<SkuItemSaleAttrVo> skuItemSaleAttrVos = skuSaleAttrValueService.getSaleAttrBySpuId(res.getSpuId());
            itemVo.setSaleAttr(skuItemSaleAttrVos);
        }, threadPoolExecutor);

        CompletableFuture<Void> descFuture = skuInfoEntityCompletableFuture.thenAcceptAsync(res -> {
            //获取spu的介绍
            SpuInfoDescEntity spuInfoDesc = spuInfoDescService.getById(res.getSpuId());
            itemVo.setDesc(spuInfoDesc);
        }, threadPoolExecutor);

        CompletableFuture<Void> baseAttrFuture = skuInfoEntityCompletableFuture.thenAcceptAsync(res -> {
            //获取spu规格参数
            List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
            itemVo.setGroupAttrs(attrGroupVos);
        }, threadPoolExecutor);

        CompletableFuture<Void> imagCompletableFuture = CompletableFuture.runAsync(() -> {
            //sku的=图片信息的获取
            List<SkuImagesEntity> skuImagesEntityList = skuImagesService.getImagesBySkuId(skuId);
            itemVo.setSkuImages(skuImagesEntityList);
        }, threadPoolExecutor);
        //等所有future完成
        CompletableFuture.allOf(saleAttrFuture,descFuture,baseAttrFuture,imagCompletableFuture).get();

        return itemVo;
    }

}