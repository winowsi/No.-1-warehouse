package com.winowsi.search.controller;

import com.winowsi.common.exception.BizCodeExceptionEnume;
import com.winowsi.common.to.es.SkuEsModel;
import com.winowsi.common.utils.R;
import com.winowsi.search.service.ProductSaveUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/21 14:41
 */
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSaveController {
    @Autowired
    private ProductSaveUpService productSaveService;

    /**
     *
     * @param skuEsModels
     * @return
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b=false;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.error("ElasticSaveController商品上架错误",e);
        }
        if (!b){
            return R.ok();
        }else {
            return R.error(BizCodeExceptionEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeExceptionEnume.PRODUCT_UP_EXCEPTION.getMsg() );
        }

    }
}
