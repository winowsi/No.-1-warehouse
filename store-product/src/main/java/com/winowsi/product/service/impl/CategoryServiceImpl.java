package com.winowsi.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.product.dao.CategoryDao;
import com.winowsi.product.entity.CategoryEntity;
import com.winowsi.product.service.CategoryService;


/**
 * @author Tom
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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