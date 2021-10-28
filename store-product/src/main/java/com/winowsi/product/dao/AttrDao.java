package com.winowsi.product.dao;

import com.winowsi.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:03:34
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> getSearchList(@Param("attrAll") List<Long> attrAll);
}
