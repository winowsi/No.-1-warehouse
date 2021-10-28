package com.winowsi.product;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.winowsi.product.entity.BrandEntity;
import com.winowsi.product.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
class StoreProductApplicationTests {
    @Autowired
    public BrandService service;

    @Test
    public void fands() {

        service.list(new QueryWrapper<BrandEntity>().eq("brand_id",1L)).forEach((brandEntity)->{
            System.out.println(brandEntity);
        });

    }

    @Test
    public void fand() {
        List<String> a = null;
        Thread thread = new Thread();
        
        System.out.println(a.size());
    }
    int foo(int n) {
        if (n < 2) return n;
       int foo = foo(n - 1);
        //int foo1 = foo(n - 2);
        return  foo;
    }

}

