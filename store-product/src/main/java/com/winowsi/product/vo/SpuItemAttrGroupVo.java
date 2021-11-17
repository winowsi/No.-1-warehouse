package com.winowsi.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/15 15:17
 */
@Data
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<SpuBaseAttrVo> baseAttrVo;

}
