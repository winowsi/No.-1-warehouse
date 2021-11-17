package com.winowsi.product.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/13 10:51
 */
@Data
@NoArgsConstructor
public class BrandRelationVo {
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌name
     */
    private String brandName;
}
