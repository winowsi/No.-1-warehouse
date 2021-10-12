package com.winowsi.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.winowsi.product.entity.AttrEntity;
import com.winowsi.product.service.AttrAttrgroupRelationService;
import com.winowsi.product.service.AttrService;
import com.winowsi.product.service.CategoryService;
import com.winowsi.product.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.winowsi.product.entity.AttrGroupEntity;
import com.winowsi.product.service.AttrGroupService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.R;



/**
 * 属性分组
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:03:34
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list/{categoryId}")
    public R list(@RequestParam Map<String, Object> params,@PathVariable Long categoryId){
        PageUtils page = attrGroupService.queryPage(params,categoryId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long attr = attrGroup.getAttrGroupId();
        Long [] findCateLogPath= categoryService.findCateLogPath(attr);
        attrGroup.setCatelogPath(findCateLogPath);
        return R.ok().put("attrGroup", attrGroup);
    }


    /**
     * 添加分组关联属性
     * @param attrGroupRelationVo
     * @return
     */
    @PostMapping("/attr/relation")
    public  R addRelation(@RequestBody AttrGroupRelationVo [] attrGroupRelationVo){

         attrAttrgroupRelationService.saveBatch(attrGroupRelationVo);
        return R.ok();
    }
    /**
     * 移除分组关联的属性
     * @param attrGroupRelationVo
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] attrGroupRelationVo){
        attrService.deleteRelation(attrGroupRelationVo);
        return R.ok();
    }

    /**
     * 查询分组关联的所有属性
     * @param attrGroupId
     * @return
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public  R attrRelation(@PathVariable("attrGroupId")Long attrGroupId ){
       List<AttrEntity> attrEntityList=attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data",attrEntityList);
    }

    /**
     * 查询分组未关联的所有属性
     * @param params
     * @param attrGroupId
     * @return
     */
    @GetMapping("/{attrGroupId}/noattr/relation")
    public  R attrNoRelation(@RequestParam Map<String, Object> params,@PathVariable("attrGroupId")Long attrGroupId ){
        PageUtils pages=  attrService.getNoRelationAttr(params,attrGroupId);
        return R.ok().put("page",pages);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
