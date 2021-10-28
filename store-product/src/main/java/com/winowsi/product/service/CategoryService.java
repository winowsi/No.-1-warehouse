package com.winowsi.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.product.entity.CategoryEntity;
import com.winowsi.product.vo.Catalogs2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:03:34
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * description:
     *  1.查出所有分类
     *  2.组装树形结构
     * @return CategoryEntity集合
     * @Aouth zhaoYao
     * @date 2021年9月26日16:58:04
     */
    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    /**
     *
     * @param attr
     * @return找的catelogId的完整路径
     * [父/子]
     */
    Long[] findCateLogPath(Long attr);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getLevel1Categories();

    Map<String, List<Catalogs2Vo>> getCatalogJson();
}

