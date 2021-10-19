package com.winowsi.product.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.winowsi.product.entity.ProductAttrValueEntity;
import com.winowsi.product.entity.SpuInfoEntity;
import com.winowsi.product.service.ProductAttrValueService;
import com.winowsi.product.vo.AttrRespVo;
import com.winowsi.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.winowsi.product.service.AttrService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.R;



/**
 * 商品属性
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:03:34
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueService productAttrValueService;


    /**
     * 根据spuId修改属性
     * @param spuId
     * @return
     */
    @PostMapping("/update/{spuId}")
    public R updateForSpu(@PathVariable("spuId") Long spuId,@RequestBody List<ProductAttrValueEntity> list){
        productAttrValueService.updateBySpuId(spuId,list);
        return R.ok();
    }
    /**
     * 根据spuId获取spu规格
     * @param spuId
     * @return
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R baseListForSpu(@PathVariable("spuId") Long spuId){

       List<ProductAttrValueEntity> productAttrValueEntities= productAttrValueService.fandAttrValueBySpuId(spuId);



        return R.ok().put("data",productAttrValueEntities);
    }
    /**
     * @description: 规格参数 销售属性
     * url:
     *  /product/attr/sale/list/{catelogId}
     *  /product/attr/base/list/{catelogId}
     * @param params
     * @param catelogId
     * @return
     */
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId,@PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,attrType);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
      AttrRespVo attrRespVo= attrService.getAttrInfor(attrId);

        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }



}
