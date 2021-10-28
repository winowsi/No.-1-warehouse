package com.winowsi.ware.service.impl;

import com.sun.xml.internal.bind.v2.TODO;
import com.winowsi.common.utils.R;
import com.winowsi.ware.feign.ProductFeignService;
import com.winowsi.ware.vo.SkuHasStockVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.ware.dao.WareSkuDao;
import com.winowsi.ware.entity.WareSkuEntity;
import com.winowsi.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wareSkuEntityQueryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        String value="0";
        if(StringUtils.isNotEmpty(skuId)&&!value.equalsIgnoreCase(skuId)){
            wareSkuEntityQueryWrapper.eq("sku_id",skuId);
        }
        if(StringUtils.isNotEmpty(wareId)&&!value.equalsIgnoreCase(wareId)){
            wareSkuEntityQueryWrapper.eq("ware_id",wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wareSkuEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> wareSkuEntities = this.baseMapper.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (wareSkuEntities.size()==0||wareSkuEntities==null){
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            //TODO 远程调用product 获取sku的信息 如果失败整个事务不会回滚
            //1.catch掉了异常所以不会回滚
            //2.TODO  分布式? 事务
            try {
                R info = productFeignService.info(skuId);
                if (info.grtCode()==0){
                    Map<String,Object> map  = (Map<String, Object>) info.get("skuInfo");
                    wareSkuEntity.setSkuName((String) map.get("skuName"));
                }
            }catch (Exception e){

            }

            this.baseMapper.insert(wareSkuEntity);
        }else {
            this.baseMapper.addStock( skuId,  wareId,  skuNum);
        }

    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuId) {

        List<SkuHasStockVo> collect = skuId.stream().map(item -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            skuHasStockVo.setSkuId(item);
            //查询
            Long count=this.baseMapper.getHasStock(item);
            skuHasStockVo.setHasStock(count==null?false:count>0);
            return skuHasStockVo;
        }).collect(Collectors.toList());
        return collect;
    }

}