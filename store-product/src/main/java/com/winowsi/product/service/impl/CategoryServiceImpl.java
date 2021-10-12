package com.winowsi.product.service.impl;

import com.winowsi.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.product.dao.CategoryDao;
import com.winowsi.product.entity.CategoryEntity;
import com.winowsi.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Tom
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * description:
     *  1.查出所有分类
     *  2.组装树形结构
     *  2.1.查到所有的一级分类
     * @return CategoryEntity集合
     * @Aouth zhaoYao
     * @date 2021年9月26日16:58:04
     */
    @Override
    public List<CategoryEntity> listWithTree() {

        List<CategoryEntity> entities = baseMapper.selectList(null);

        List<CategoryEntity> collect = entities.stream()
                .filter(categoryEntity->categoryEntity.getParentCid() == 0)
                .map((menu)->{
                    menu.setChildren(getChildren(menu,entities));
                    return menu;
                })
                .sorted((beforeMenu,afterMenu)->beforeMenu.getSort()-afterMenu.getSort())
                .collect(Collectors.toList());

        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {

        baseMapper.deleteBatchIds(asList);
    }

    /**
     *
     * @param attr
     * @return 找到三级分类的完整路径
     */
    @Override
    public Long[] findCateLogPath(Long attr) {
        List<Long> longs = new ArrayList<>();

        List<Long> prentPath = findPrentPath(attr, longs);

        Collections.reverse(prentPath);

        return prentPath.toArray(new Long[prentPath.size()]);
    }

    /**
     *级联更新
     * @param category
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())){
            categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
        }
    }

    private List<Long> findPrentPath(Long prentId, List<Long> longs){

        longs.add(prentId);

        CategoryEntity category = this.getById(prentId);
        if (category.getParentCid()!=0){
            findPrentPath(category.getParentCid(),longs);
        }
        return longs;

    }


    /**
     *
     * @param root 当前菜单
     * @param all 包含当前菜单的所有菜单集合
     * @return 返回查找的子菜单
     */
    private List<CategoryEntity> getChildren(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> collect = all.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(root.getCatId()))
                .map((categoryEntity) -> {
                    //找的子菜单
                    categoryEntity.setChildren(getChildren(categoryEntity, all));
                    return categoryEntity;
                })
                .sorted((mue1, mue2) -> {
                    //菜单的排序
                    return (mue1.getSort()==null?0:mue1.getSort()) - (mue2.getSort()==null?0:mue2.getSort());
                })
                .collect(Collectors.toList());
        return collect;

    }

}