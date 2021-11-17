package com.winowsi.product.service.impl;

import com.winowsi.product.dao.AttrAttrgroupRelationDao;
import com.winowsi.product.entity.AttrEntity;
import com.winowsi.product.service.AttrService;
import com.winowsi.product.vo.AttrGroupWithAttrsVo;
import com.winowsi.product.vo.SkuItemVo;
import com.winowsi.product.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
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

import com.winowsi.product.dao.AttrGroupDao;
import com.winowsi.product.entity.AttrGroupEntity;
import com.winowsi.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        // 2021年10月8日18:00:23
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> attrGroupEntityQueryWrapper = new QueryWrapper<AttrGroupEntity>();
        if (!StringUtils.isEmpty(key)){
            attrGroupEntityQueryWrapper.and((wapper)->{
                wapper.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }

        if (categoryId==0){
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    attrGroupEntityQueryWrapper
            );

            return new PageUtils(page);
        }else {

            attrGroupEntityQueryWrapper .eq("catelog_id",categoryId);

            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), attrGroupEntityQueryWrapper);
            return new PageUtils(page);
        }

    }

    /**
     * //            Long attrGroupId = item.getAttrGroupId();
     * //            List<AttrAttrgroupRelationEntity> attrgroupRelationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupId));
     * //            List<Long> attrId = attrgroupRelationEntities.stream().map(attrIds -> attrIds.getAttrId()).collect(Collectors.toList());
     * //            List<AttrEntity> attrEntityList = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", attrId));
     * //            attrGroupWithAttrs.setAttrs(attrEntityList);
     * 根据分类Id查询所有的分组的所有属性
     * @param categoryId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsById(Long categoryId) {
        //1.所有的组信息
        List<AttrGroupEntity> attrGroupEntities = this.baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", categoryId));

        //1.1所有组的组id
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(item -> {
            AttrGroupWithAttrsVo attrGroupWithAttrs = new AttrGroupWithAttrsVo();
            //将已有的属性拷贝到新的vo中
            BeanUtils.copyProperties(item,attrGroupWithAttrs);
            List<AttrEntity> relationAttr = attrService.getRelationAttr(item.getAttrGroupId());
            attrGroupWithAttrs.setAttrs(relationAttr);
            return attrGroupWithAttrs;
        }).collect(Collectors.toList());

        return collect;
    }

    /***
     * 查出当前spu对应的属性分组信息以及当前分组所对应的属性的值
     *
     * @param
     * @param catLogId id
     * @return 组名和值
     */
    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catLogId) {
        return baseMapper.getAttrGroupWithAttrsBySpuId(spuId,catLogId);
    }

}