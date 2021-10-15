package com.winowsi.product.service.impl;

import com.winowsi.common.to.SkuReductionTo;
import com.winowsi.common.to.SpuBoundsTo;
import com.winowsi.common.utils.R;
import com.winowsi.product.entity.*;
import com.winowsi.product.feign.CouponSpuFeignService;
import com.winowsi.product.service.*;
import com.winowsi.product.vo.spuvo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private SpuImagesService spuImagesService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private  SkuInfoService skuInfoService;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    CouponSpuFeignService couponSpuFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    /**
     * @param vo 保存商品信息的实体类
     * @description: 保存商品的所有属性
     * @TODO 初级-高级完善2021年10月14日17:40:26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveInfoSpuVo(SpuSaveVo vo) {
        //1.保存spu基本信息;pms_spu_info
        SpuInfoEntity spuInfo = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfo);
        spuInfo.setCreateTime(new Date());
        spuInfo.setUpdateTime(new Date());
        this.saveBaseSupInfo(spuInfo);
        //2.保存spu的图片描述信息;pms_spu_info_desc
        List<String> decrypt = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfo.getId());
        spuInfoDescEntity.setDecript(String.join(",", decrypt));
        spuInfoDescService.saveInforDecript(spuInfoDescEntity);
        //3.保存spu的图片集;pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfo.getId(), images);
        //4.保存spu的规格参数;pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attrs -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attrs.getAttrId());
            productAttrValueEntity.setSpuId(spuInfo.getId());
            AttrEntity attrEntity = attrService.getById(attrs.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            productAttrValueEntity.setAttrValue(attrs.getAttrValues());
            productAttrValueEntity.setQuickShow(attrs.getShowDesc());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);

        //5.保存当前spu的所有sku信息
        List<Skus> sku = vo.getSkus();
        if (sku!=null&&sku.size()!=0){
            for (Skus sk : sku) {
                String DefaultImg= "";
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sk,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfo.getBrandId());
                skuInfoEntity.setCatalogId(spuInfo.getCatalogId());
                skuInfoEntity.setSpuId(spuInfo.getId());
                skuInfoEntity.setSaleCount(0L);
                for (Images image : sk.getImages()) {
                    if (image.getDefaultImg()==1){
                        DefaultImg=image.getImgUrl();
                    }
                }
                skuInfoEntity.setSkuDefaultImg(DefaultImg);
                //5.1) ..sku的基本信息;pms_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImagesEntity> skuImagesEntityList = sk.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity->StringUtils.isNotEmpty(entity.getImgUrl())).collect(Collectors.toList());
                //5.2) ..sku的图片信息;pms_sku_images
                skuImagesService.saveBatch(skuImagesEntityList);

                //5.3) ..sku的销售属性;pms_sku_sale_attr_value
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = sk.getAttr().stream().map(att -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(att, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                //5.3) ..sku的销售属性;pms_sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntityList);

                //5.4) ..sku的优惠满减,会员,打折信息跨库;store_sms->(/折扣->sms_sku_ladder/满减->sms_sku_full_reduction/会员->sms_member_price)
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sk,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (sk.getFullCount()>0||sk.getFullPrice().compareTo(new BigDecimal("0"))==1){
                    R r = couponSpuFeignService.saveReduction(skuReductionTo);
                    if (r.grtCode()!=0){
                        log.error("远程保存spu积分信息失败");
                    }

                }


            }
        }
        //5.保存spu的积分信息;store_sms->(sms_spu_bounds)
        Bounds bounds = vo.getBounds();
        SpuBoundsTo spuBoundsTo=new SpuBoundsTo();
        BeanUtils.copyProperties(bounds,spuBoundsTo);
        spuBoundsTo.setSpuId(spuInfo.getId());
        R r = couponSpuFeignService.saveBounds(spuBoundsTo);
        if (r.grtCode()!=0){
            log.error("远程保存spu优惠信息失败");
        }

    }

    @Override
    public void saveBaseSupInfo(SpuInfoEntity spuInfo) {
        this.baseMapper.insert(spuInfo);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");
        String value="0";
        QueryWrapper<SpuInfoEntity> spuInfoQueryWrapper = new QueryWrapper<>();
        /**
         * key:
         * status: 0
         * brandId: 6
         * catelogId: 225
         */
        if (StringUtils.isNotEmpty(key)){
            spuInfoQueryWrapper.and(w->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        if (StringUtils.isNotEmpty(status)){
            spuInfoQueryWrapper.eq("publish_status",status);
        }
        if (StringUtils.isNotEmpty(brandId)&&!value.equalsIgnoreCase(brandId)){
            spuInfoQueryWrapper.eq("brand_id",brandId);
        }
        if (StringUtils.isNotEmpty(catelogId)&&!value.equalsIgnoreCase(catelogId)){
            spuInfoQueryWrapper.eq("catalog_id",catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                spuInfoQueryWrapper
        );

        return new PageUtils(page);
    }

}