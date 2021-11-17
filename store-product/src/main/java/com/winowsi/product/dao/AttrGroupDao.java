package com.winowsi.product.dao;

import com.winowsi.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.winowsi.product.vo.SkuItemVo;
import com.winowsi.product.vo.SpuItemAttrGroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:03:34
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {


    List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(@Param("spuId") Long spuId, @Param("catLogId") Long catLogId);
}
