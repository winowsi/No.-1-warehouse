package com.winowsi.product.web;

import com.winowsi.product.entity.CategoryEntity;
import com.winowsi.product.service.CategoryService;
import com.winowsi.product.vo.Catalogs2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/22 12:35
 */
@Controller
public class IndexController {
    @Autowired
    private CategoryService categoryService;

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

}
