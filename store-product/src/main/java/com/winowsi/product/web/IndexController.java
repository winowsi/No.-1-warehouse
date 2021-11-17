package com.winowsi.product.web;

import com.winowsi.product.entity.CategoryEntity;
import com.winowsi.product.service.CategoryService;
import com.winowsi.product.vo.Catalogs2Vo;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/22 12:35
 */
@Controller
public class IndexController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = {"/","/index"})
    public String getIndex(Model model){
        //1、查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categories();
        model.addAttribute("categories", categoryEntities);
        return "index";
    }
    @GetMapping(value = "/index/catalog.json")
    @ResponseBody
    public Map<String, List<Catalogs2Vo>> getCatalogJson() {
        return categoryService.getCatalogJson();
    }


    @GetMapping("/write")
    @ResponseBody
    public  String writeLock(){
        String s = "";
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("my-lock");
        RLock rLock = readWriteLock.writeLock();
        try {
            rLock.lock();
            Thread.sleep(3000);
            s = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set("s",s);
        } catch (InterruptedException e) {
            System.out.println(e);
        }finally {
            rLock.unlock();
        }

        return s;
    }
    @GetMapping("/read")
    @ResponseBody
    public  String readLock(){
        String s = "";
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("my-lock");
        RLock rLock = readWriteLock.readLock();
        try {
            rLock.lock();
            s=stringRedisTemplate.opsForValue().get("s");
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            rLock.unlock();
        }

        return s;
    }



}
