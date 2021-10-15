package com.winowsi.coupon.service.impl;

import com.winowsi.common.to.MemberPrice;
import com.winowsi.common.to.SkuReductionTo;
import com.winowsi.coupon.entity.MemberPriceEntity;
import com.winowsi.coupon.entity.SkuLadderEntity;
import com.winowsi.coupon.service.MemberPriceService;
import com.winowsi.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.coupon.dao.SkuFullReductionDao;
import com.winowsi.coupon.entity.SkuFullReductionEntity;
import com.winowsi.coupon.service.SkuFullReductionService;

/**
 * @author Tom
 */
@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {
    @Autowired
    private SkuLadderService skuLadderService;
    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    /**
     *
     * @param skuReductionTo
     */
    @Override
    public void saveReduction(SkuReductionTo skuReductionTo) {
        //5.4) ..sku的优惠满减,会员,打折信息跨库;store_sms->(/折扣->sms_sku_ladder/满减->sms_sku_full_reduction/会员->sms_member_price)
        //1.满减->sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo,skuFullReductionEntity);
        if (skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal("0"))==1){
            this.baseMapper.insert(skuFullReductionEntity);
        }

        //2.折扣->sms_sku_ladder
        SkuLadderEntity skuLadderEntity=new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTo,skuLadderEntity);
        if (skuLadderEntity.getFullCount()>0){
            skuLadderService.save(skuLadderEntity);
        }
        //3.会员->sms_member_price
        List<MemberPriceEntity> collect = skuReductionTo.getMemberPrice().stream().map(memberPrice -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setMemberPrice(memberPrice.getPrice());
            memberPriceEntity.setId(memberPrice.getId());
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setAddOther(1);
            memberPriceEntity.setMemberLevelName(memberPrice.getName());
            return memberPriceEntity;
        }).filter(item->{
            return item.getMemberPrice().compareTo(new BigDecimal("0")) == 1;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(collect);


    }

}