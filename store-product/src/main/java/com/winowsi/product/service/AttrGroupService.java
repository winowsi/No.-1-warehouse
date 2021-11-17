package com.winowsi.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.product.entity.AttrGroupEntity;
import com.winowsi.product.vo.AttrGroupWithAttrsVo;
import com.winowsi.product.vo.SpuItemAttrGroupVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:03:34
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long categoryId);

    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsById(Long categoryId);

    /**
     * 属性分组信息
     * @param spuId spuID
     * @param catLogId 三级分类Id
     * @return List<SkuItemVo.SpuItemAttrGroupVo>
     */
    List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catLogId);
}

