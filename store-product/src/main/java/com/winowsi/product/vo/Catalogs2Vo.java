package com.winowsi.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/22 13:40
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catalogs2Vo {
    /**
     * 一级父分类的id
     */
    private String catalog1Id;
    /**
     * 当前二级分类的id
     */
    private String id;
    /**
     * 当前二级分类的NAme
     */
    private String name;
    /**
     * 三级子分类
     */
    private List<Category3Vo> catalog3List;

    /**
     * 三级分类vo
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category3Vo {

        /**
         * 父分类、二级分类id
         */
        private String catalog2Id;

        private String id;

        private String name;
    }
}
