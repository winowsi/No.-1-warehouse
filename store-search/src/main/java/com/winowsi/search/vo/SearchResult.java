package com.winowsi.search.vo;

import com.winowsi.common.to.es.SkuEsModel;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

/**
 * @description: 描述 将检索到的数据响应给页面
 * @author: ZaoYao
 * @time: 2021/11/8 10:22
 */
@Data
public class SearchResult {
    /**
     * 所有商品信息
     */
    private List<SkuEsModel> products;
    /**
     * 当前页码
     */
    private  Integer pageNumber;
    /**
     *总记录数
     */
    private  Long total;
    /**
     * 总页码
     */
    private Integer totalPages;
    /**
     * 导航页码
     */
    private  List<Integer> pageNavs;
    /**
     * 当前检索到的结果所包含的所有品牌
     */
    private List<BrandVo> brands;
    /**
     * 当前检索到的结果所包含的所有属性
     */
    private List<AttrVo> attrs;
    /**
     * 当前检索到的结果所包含的所有分类
     */
    private List<CatalogVo> catalogs;
    /**
     *面包屑导航
     */
    private List<NavVo> navs=new ArrayList<>();
    @Data
    public static  class NavVo{
        private  String navName;
        private  String navValue;
        private  String link;
    }

    /**
     * 分类的vo
     */
    @Data
    public static class CatalogVo{
        /**
         * 分类的Id
         */
        private Long catalogId;
        /**
         * 分类的名字
         */
        private String catalogName;
    }

    /**
     * 品牌的vo
     */
    @Data
    public static class BrandVo{
        /**
         * 品牌id
         */
        private Long brandId;
        /**
         *品牌名字
         */
        private String brandName;
        /**
         * 品牌log
         */
        private String brandImg;
    }

    /**
     * 属性的vo
     */
    @Data
    public static class AttrVo{
        /**
         * 属性id
         */
        private Long attrId;
        /**
         *属性名字
         */
        private String attrName;
        /**
         *属性对应的可选值
         */
        private List<String> attrValue;
    }
}
