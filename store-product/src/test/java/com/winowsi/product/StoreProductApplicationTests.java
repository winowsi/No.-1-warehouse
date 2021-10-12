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


}
