package com.winowsi.product.vo;

import lombok.Data;

/**
 * @description: AttrVo 响应数据
 * @Author:ZaoYao
 * @Time: 2021/10/11 13:34
 */
@Data
public class AttrRespVo extends AttrVo {
    /**
     * 所在分类的名字
     */
    private String catelogName;

    /**
     * 所在分组的名字
     */
    private  String groupName;

    /**
     * 所在分类的完整路径
     */
    private Long[] catelogPath;
}
