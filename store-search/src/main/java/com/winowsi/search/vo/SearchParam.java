package com.winowsi.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @description: 封装页面所有可能传递过来的查询条件
 * @author: ZaoYao
 * @time: 2021/11/8 9:46
 */
@Data
public class SearchParam {
    /**
     * 页面传过来全文匹配的检索关键字
     */
    private String keyword;
    /**
     * 三级分类的id
     */
    private  Long catalog3Id;
    /**
     * 排序条件
     * sort=saleCount_asc 销量升序
     * sort=saleCount_desc 销量降序
     *
     * sort=skuPrice_asc 价格升序
     * sort=skuPrice_desc 价格降序
     *
     * sort=hostScore_desc 热度降序
     * sort=hostScore_asc  热度升序
     */
    private  String sort;
    /**
     * 是否只显示有货
     * hasStock=0 or 1
     */
    private  Integer hasStock=1;
    /**
     * 价格区间
     * 1_500
     * _500以内
     * 500_以上
     */
    private  String skuPrice;
    /**
     * 按照品牌查询
     * 可多选
     * 品牌id
     */
    private List< Long > branId;
    /**
     * 按照属性查询
     * 可多选
     * attrs=1_5寸:8寸&attrs=2_16G:8G
     */
    private List<String> attrs;
    /**
     * 页码
     */
    private  Integer pageNumber=1;
    /**
     * 查询条件
     */
    private  String queryString;
}
