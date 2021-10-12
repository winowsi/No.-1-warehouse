package com.winowsi.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.product.entity.AttrGroupEntity;
import org.apache.ibatis.annotations.Mapper;

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
}

