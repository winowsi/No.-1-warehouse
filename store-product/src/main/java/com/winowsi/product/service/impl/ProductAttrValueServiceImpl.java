package com.winowsi.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.product.dao.ProductAttrValueDao;
import com.winowsi.product.entity.ProductAttrValueEntity;
import com.winowsi.product.service.ProductAttrValueService;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> collect) {
        this.saveBatch(collect);
    }

    @Override
    public List<ProductAttrValueEntity> fandAttrValueBySpuId(Long spuId) {

        return this.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
    }

    @Override
    public void updateBySpuId(Long spuId, List<ProductAttrValueEntity> list) {
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        List<ProductAttrValueEntity> collect = list.stream().map(item -> {item.setSpuId(spuId); return item;}).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}