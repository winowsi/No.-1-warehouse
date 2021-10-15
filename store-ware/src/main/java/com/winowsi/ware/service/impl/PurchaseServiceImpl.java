package com.winowsi.ware.service.impl;

import com.winowsi.common.constant.WareConstant;
import com.winowsi.ware.entity.PurchaseDetailEntity;
import com.winowsi.ware.service.PurchaseDetailService;
import com.winowsi.ware.service.WareSkuService;
import com.winowsi.ware.vo.ItemDoneVo;
import com.winowsi.ware.vo.MergeVo;
import com.winowsi.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.ware.dao.PurchaseDao;
import com.winowsi.ware.entity.PurchaseEntity;
import com.winowsi.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    private PurchaseDetailService purchaseDetailService;
    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> purchaseEntityQueryWrapper = new QueryWrapper<>();
        purchaseEntityQueryWrapper.eq("status","0").or().eq("status","1");
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                purchaseEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId==null){
            PurchaseEntity purchase=new PurchaseEntity();
            purchase.setStatus(WareConstant.purchaseStatusEnum.CREATED.getCode());
            purchase.setCreateTime(new Date());
            purchase.setUpdateTime(new Date());
            this.baseMapper.insert(purchase);
            purchaseId=purchase.getId();
        }

        //采购单的状态是0和1才可以合并
        if (purchaseId!=null){
            PurchaseEntity  purchaseEntity = this.getById(purchaseId);
            boolean created = purchaseEntity.getStatus().equals(WareConstant.purchaseDetailEnum.CREATED.getCode());
            boolean assigned = purchaseEntity.getStatus().equals(WareConstant.purchaseDetailEnum.ASSIGNED);
            if (created||assigned){
                List<Long> items = mergeVo.getItems();
                Long finalPurchaseId = purchaseId;
                List<PurchaseDetailEntity> collect = items.stream().map(i -> {
                    PurchaseDetailEntity purchaseDetail = new PurchaseDetailEntity();
                    purchaseDetail.setPurchaseId(finalPurchaseId);
                    purchaseDetail.setId(i);
                    purchaseDetail.setStatus(WareConstant.purchaseDetailEnum.ASSIGNED.getCode());

                    return purchaseDetail;
                }).collect(Collectors.toList());
                purchaseDetailService.updateBatchById(collect);

                PurchaseEntity purchase =new PurchaseEntity();
                purchase.setUpdateTime(new Date());
                this.updateById(purchase);

            }
        }

    }

    /**
     *
     * @param ids 采购单的id
     */
    @Override
    public void received(List<Long> ids) {
        //1.确定当前采购单是新建或已分配状态
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter((f -> {
            if (f.getStatus().equals(WareConstant.purchaseStatusEnum.CREATED.getCode()) || f.getStatus().equals(WareConstant.purchaseStatusEnum.ASSIGNED.getCode())) {
                return true;
            } else {
                return false;
            }
        })).map(item -> {
            item.setStatus(WareConstant.purchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        //2.改变采购单的状态
        this.updateBatchById(collect);

        //3.改变采购单采购项的状态
        collect.forEach((item)->{
          List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listDetaliByPurchas(item.getId());
            List<PurchaseDetailEntity> detailEntities = purchaseDetailEntities.stream().map(purchaseDetailEntity -> {
                PurchaseDetailEntity purchaseDetail = new PurchaseDetailEntity();
                purchaseDetail.setId(purchaseDetailEntity.getId());
                purchaseDetail.setStatus(WareConstant.purchaseDetailEnum.BUYING.getCode());
                return purchaseDetail;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntities);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void done(PurchaseDoneVo doneVo) {
        Long id = doneVo.getId();
        //2.改变采购项状
        Boolean flag = true;
        List<ItemDoneVo> items = doneVo.getItems();
        List<PurchaseDetailEntity> purchaseDetailEntities = new ArrayList<>();
        for (ItemDoneVo item : items) {
            PurchaseDetailEntity purchaseDetail=new PurchaseDetailEntity();
            if ( item.getStatus().equals(WareConstant.purchaseDetailEnum.HAS_ERROR.getCode())){
                flag =false;
                purchaseDetail.setStatus(item.getStatus());
            }else {
                purchaseDetail.setStatus(WareConstant.purchaseDetailEnum.FINISH.getCode());
                //3.成功后入库
                PurchaseDetailEntity detailEntity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(detailEntity.getSkuId(),detailEntity.getWareId(),detailEntity.getSkuNum());

            }
            purchaseDetailEntities.add(purchaseDetail);
        }
        purchaseDetailService.updateBatchById(purchaseDetailEntities);

        //1.改变采购单状态

        PurchaseEntity purchase=new PurchaseEntity();
        purchase.setId(id);
        purchase.setStatus(flag?WareConstant.purchaseDetailEnum.FINISH.getCode():WareConstant.purchaseDetailEnum.HAS_ERROR.getCode());
        purchase.setUpdateTime(new Date());
        this.updateById(purchase);

    }

}