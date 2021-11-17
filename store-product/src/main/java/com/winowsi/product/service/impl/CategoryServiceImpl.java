package com.winowsi.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.winowsi.product.service.CategoryBrandRelationService;
import com.winowsi.product.vo.Catalogs2Vo;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
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
     *@Caching(evict ={ @CacheEvict(value = "Category",key = "'getLevel1Categories'"), @CacheEvict(value = "Category",key = "'getCatalogJson'")})
     *@CacheEvict(value = "Category",allEntries = true )::清除Category标识下分区的所有缓存数据
     * 同时操作多个缓存数据
     * @CacheEvict清除多个缓存::(失效模式->=清除缓存重新查的时候再放入缓存)
     * @CachePut 将修改返回的值再写入缓存(双写模式)
     * @param category
     */

    @CacheEvict(value = "Category",allEntries = true )
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }
    }

    /**                                           |
     * spring-boot-starter-cache-->@EnableCaching v
     * spring.cache.type:redis
     * spring.cache.redis:time-to-live: 36000
     * @Cacheable (value = "Category",key = "'Level1Categories'")
     *代表当前方法的结果需要缓存,如果缓存中有数据则直接返回,没有的话就去数据库查查到放入缓存再返回数据
     * value = "Category"{缓存分区按照业务类型进行分区}
     * key = "'Level1Categories'"{缓存的的名字simpleKey(自主生成的key值)}
     *  默认行为
     *  1/缓存中有方法不用调用
     *  2/key默认生成缓存的名字SimpleKey[]{自主生成的key值}
     *  3/缓存的value值默认使用jdk序列化机制,将序列化后的数据存到redis中
     *  4/默认ttl(过期时间)的时间为-1
     *  自定义
     *  1-/指定生成缓存的key:key属性指定,接受spEl(动态取值)::key = "#root.methodName",普通字符串需要加单引号
     *  2-/指定缓存中的数据的过期时间,配置文件中修改ttl
     *  3-/将数据保存成json格式
     * @return 一级分类的所有json数据
     */
    @Cacheable(value = "Category",key = "#root.methodName")
    @Override
    public List<CategoryEntity> getLevel1Categories() {
        return this.baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

    //TODO 队外内存溢出

    /**
     * 队外内存溢出
     * 1.产生的原因 lettuce 作为操作redis的客户端.他使用了netty进行网络通信
     * 2.lettuce的bug导致netty的堆内存溢出lettuce没有正确的调用netty的释放内存的方法
     * 3.解决办法:升级lettuce或者使用jedis代替lettuce
     * <p>
     * 高并发下的其他问题
     * 1.缓存击穿(一直请求不存在的数据):给缓存存入null值
     * 2.缓存雪崩(缓存出现大面积的数据同时失效):给缓存中的数据设置失效时间+随机时间
     * 3.缓存击穿(多个线程同时访问同一个刚好过期key值的数据):加锁控制
     */

    @Cacheable(value = "Category",key = "#root.methodName",sync = true)
    @Override
    public Map<String, List<Catalogs2Vo>> getCatalogJson() {
        //查出所有的分类
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(new QueryWrapper<>(null));
        //查出所有的一级分类
        List<CategoryEntity> level1Categories = getParent_cid(categoryEntities, 0L);
        //封装
        Map<String, List<Catalogs2Vo>> catalogs = level1Categories.stream().collect(Collectors.toMap(l1 -> l1.getCatId().toString(), l1 -> {
            //查出1级分类的二级分类
            List<CategoryEntity> entities = getParent_cid(categoryEntities, l1.getCatId());
            List<Catalogs2Vo> level2Categories = null;
            if (entities != null) {
                //封装二级节点
                level2Categories = entities.stream().map(l2 -> {
                    Catalogs2Vo catalogs2Vo = new Catalogs2Vo(l1.getCatId().toString(), l2.getCatId().toString(), l2.getName(), null);
                    //查出2级分类的三级分类
                    List<CategoryEntity> entities1 = getParent_cid(categoryEntities, l2.getCatId());
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
        return catalogs;

    }

    /**
     *
     * 手动
     * 1/放入缓存
     * 2/序列化返回缓存中数据
     * @return
     */
    public Map<String, List<Catalogs2Vo>> getCatalogJsonB() {
        //获取redis中的数据
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            //没有就去数据库查,查完再保存到redis中
            Map<String, List<Catalogs2Vo>> catalogJsonDB = getCatalogJsonDbWithRedisLock();
            String s = JSON.toJSONString(catalogJsonDB);
            stringRedisTemplate.opsForValue().set("catalogJson", s);
        }
        Map<String, List<Catalogs2Vo>> stringListMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalogs2Vo>>>() {
        });

        return stringListMap;
    }

    /**
     * 从数据库里查找三级分类
     *分布式锁->redisson 实现
     * 1/默认自动续期 时间30秒10秒触发一次
     * @return
     */
    public Map<String, List<Catalogs2Vo>> getCatalogJsonDbWithRedissonLock() {

        String s= UUID.randomUUID().toString();
        RLock lock = redissonClient.getLock(s);
        Map<String, List<Catalogs2Vo>> stringListMap=null;
        try {
            lock.lock();
            stringListMap = getStringListMap();
        } finally {
            lock.unlock();
        }

        return stringListMap;
    }

    /**
     * 从数据库里查找三级分类
     * 分布式锁->手动脚本实现
     *  1/加锁要保证原子性
     *  2/释放锁也要保证原子性
     *  3/ 拿不到锁就自旋等待
     * @return getCatalogJsonDbWithRedisLock() stringListMap
     */
    public Map<String, List<Catalogs2Vo>> getCatalogJsonDbWithRedisLock() {

        //value大字符串
        String token = UUID.randomUUID().toString();
        //lua脚本
        String lua = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                "then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        //分布式锁redis站坑位 原子性
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", token, 300, TimeUnit.SECONDS);
        if (lock) {
            Map<String, List<Catalogs2Vo>> stringListMap = null;
            try {
                stringListMap = getStringListMap();
            } finally {
                //删除锁 原子性执行脚本
                Long lock1 = stringRedisTemplate.execute(new DefaultRedisScript<>(lua, Long.class), Arrays.asList("lock"), token);
            }
            return stringListMap;
        } else {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //自旋
            return getCatalogJsonDbWithRedisLock();
        }
    }


    /**
     * 从数据库里查找三级分类
     *本地锁
     * @return
     */
    public Map<String, List<Catalogs2Vo>> getCatalogJsonDbWithLocalLock() {

        synchronized (this) {
            //TODO 本地锁 synchronized, JUC(lock) 在分布式情况下,想要锁住所有,必须使用分布式锁
            //加锁后再次判断如果缓存不为空直接返回
            return getStringListMap();
        }
    }

    /** 抽取查询三级分类的方法
     * 先查缓存->再查数据库->放入缓存->再返回查到的数据 listMap
     * @return listMap
     */
    private Map<String, List<Catalogs2Vo>> getStringListMap() {
        //加锁后再次判断如果缓存不为空直接返回
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isNotEmpty(catalogJson)) {
            Map<String, List<Catalogs2Vo>> stringListMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalogs2Vo>>>() {
            });
            return stringListMap;
        }
        //查出所有的分类
        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(new QueryWrapper<>(null));
        //查出所有的一级分类
        List<CategoryEntity> level1Categories = getParent_cid(categoryEntities, 0L);
        //封装
        Map<String, List<Catalogs2Vo>> listMap = level1Categories.stream().collect(Collectors.toMap(l1 -> l1.getCatId().toString(), l1 -> {
            //查出1级分类的二级分类
            List<CategoryEntity> entities = getParent_cid(categoryEntities, l1.getCatId());
            List<Catalogs2Vo> level2Categories = null;
            if (entities != null) {
                //封装二级节点
                level2Categories = entities.stream().map(l2 -> {
                    Catalogs2Vo catalogs2Vo = new Catalogs2Vo(l1.getCatId().toString(), l2.getCatId().toString(), l2.getName(), null);
                    //查出2级分类的三级分类
                    List<CategoryEntity> entities1 = getParent_cid(categoryEntities, l2.getCatId());
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
        String s = JSON.toJSONString(listMap);
        stringRedisTemplate.opsForValue().set("catalogJson", s);
        return listMap;
    }

    /**
     * 查出三级分类的父id相同的集合
     * @param categoryEntities
     * @param parent_cid
     * @return
     */
    private List<CategoryEntity> getParent_cid(List<CategoryEntity> categoryEntities, Long parent_cid) {
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