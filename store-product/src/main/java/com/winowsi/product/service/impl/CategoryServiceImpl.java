package com.winowsi.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.winowsi.product.service.CategoryBrandRelationService;
import com.winowsi.product.vo.Catalogs2Vo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
     * 1.查出所有分类
     * 2.组装树形结构
     * 2.1.查到所有的一级分类
     *
     * @return CategoryEntity集合
     * @Aouth zhaoYao
     * @date 2021年9月26日16:58:04
     */
    @Override
    public List<CategoryEntity> listWithTree() {

        List<CategoryEntity> entities = baseMapper.selectList(null);

        List<CategoryEntity> collect = entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map((menu) -> {
                    menu.setChildren(getChildren(menu, entities));
                    return menu;
                })
                .sorted((beforeMenu, afterMenu) -> beforeMenu.getSort() - afterMenu.getSort())
                .collect(Collectors.toList());

        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {

        baseMapper.deleteBatchIds(asList);
    }

    /**
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
     * 级联更新
     *
     * @param category
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }
    }

    @Override
    public List<CategoryEntity> getLevel1Categories() {
        return this.baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

    //TODO 队外内存溢出
    //1.产生的原因 lettuce 作为操作redis的客户端.他使用了netty进行网络通信
    //lettuce的bug导致netty的堆内存溢出
    @Override
    public Map<String, List<Catalogs2Vo>> getCatalogJson(){
        //获取redis中的数据
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)){
            //没有就去数据库查,查完再保存到redis中
            Map<String, List<Catalogs2Vo>> catalogJsonDB = getCatalogJsonDB();
            String s = JSON.toJSONString(catalogJsonDB);
            stringRedisTemplate.opsForValue().set("catalogJson",s);
        }
        Map<String, List<Catalogs2Vo>> stringListMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalogs2Vo>>>(){});

        return stringListMap;
    }

    public Map<String, List<Catalogs2Vo>> getCatalogJsonDB() {
        //查出所有的分类
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(new QueryWrapper<>(null));
        //查出所有的一级分类
        List<CategoryEntity> level1Categories = getParent_cid(categoryEntities,0L);
        //封装
        Map<String, List<Catalogs2Vo>> listMap = level1Categories.stream().collect(Collectors.toMap(l1 -> l1.getCatId().toString(), l1-> {
            //查出1级分类的二级分类
            List<CategoryEntity> entities = getParent_cid(categoryEntities,l1.getCatId());
            List<Catalogs2Vo> level2Categories = null;
            if (entities != null) {
                //封装二级节点
                level2Categories = entities.stream().map(l2 -> {
                    Catalogs2Vo catalogs2Vo = new Catalogs2Vo(l1.getCatId().toString(), l2.getCatId().toString(), l2.getName(), null);
                    //查出2级分类的三级分类
                    List<CategoryEntity> entities1 = getParent_cid(categoryEntities,l2.getCatId());
                    if (entities1 != null) {
                        //封装三级节点
                        List<Catalogs2Vo.Category3Vo> catalogs3Vo = entities1.stream().map(l3 -> {
                            Catalogs2Vo.Category3Vo category3Vo = new Catalogs2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return category3Vo;
                        }).collect(Collectors.toList());
                        catalogs2Vo.setCatalog3List(catalogs3Vo);
                    }
                    return catalogs2Vo;
                }).collect(Collectors.toList());
            }
            return level2Categories;
        }));

        return listMap;
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> categoryEntities,Long parent_cid) {
        return categoryEntities.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
    }

    private List<Long> findPrentPath(Long prentId, List<Long> longs) {

        longs.add(prentId);

        CategoryEntity category = this.getById(prentId);
        if (category.getParentCid() != 0) {
            findPrentPath(category.getParentCid(), longs);
        }
        return longs;

    }


    /**
     * @param root 当前菜单
     * @param all  包含当前菜单的所有菜单集合
     * @return 返回查找的子菜单
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> collect = all.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(root.getCatId()))
                .map((categoryEntity) -> {
                    //找的子菜单
                    categoryEntity.setChildren(getChildren(categoryEntity, all));
                    return categoryEntity;
                })
                .sorted((mue1, mue2) -> {
                    //菜单的排序
                    return (mue1.getSort() == null ? 0 : mue1.getSort()) - (mue2.getSort() == null ? 0 : mue2.getSort());
                })
                .collect(Collectors.toList());
        return collect;

    }

}