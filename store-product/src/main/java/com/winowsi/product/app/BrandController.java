package com.winowsi.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.winowsi.common.valid.AddGroup;
import com.winowsi.common.valid.UpdateGroup;
import com.winowsi.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winowsi.product.entity.BrandEntity;
import com.winowsi.product.service.BrandService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.R;


/**
 * 品牌
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-09-18 15:03:34
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }
    @RequestMapping("/infos")
    public R info(@RequestParam("brandIds") List<Long> brandId){
        List<BrandEntity> brandEntityList = brandService.list(new QueryWrapper<BrandEntity>().in("brand_id", brandId));

        return R.ok().put("brand", brandEntityList);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand ){
        brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated({UpdateGroup.class})@RequestBody BrandEntity brand){
		brandService.updateDetail(brand);

        return R.ok();
    }
    /**
     * 修改状态
     */
    @RequestMapping("/update/Status")
    public R updateStatus(@Validated({UpdateStatusGroup.class})@RequestBody BrandEntity brand){
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
