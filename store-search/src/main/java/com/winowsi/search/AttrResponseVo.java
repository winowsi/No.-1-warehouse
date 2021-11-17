package com.winowsi.search;

import lombok.Data;

/**
 * @description: 描述
 * @author: ZaoYao
 * @time: 2021/11/9 17:52
 */
@Data
public class AttrResponseVo {
    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 是否需要检索[0-不需要，1-需要]
     */
    private Integer searchType;
    /**
     * 漏掉的 是否可多选的属性0单选1多选
     */
    private Integer valueType;
    /**
     * 属性图标
     */
    private String icon;
    /**
     * 可选值列表[用逗号分隔]
     */
    private String valueSelect;
    /**
     * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
     */
    private Integer attrType;
    /**
     * 启用状态[0 - 禁用，1 - 启用]
     */
    private Long enable;
    /**
     * 所属分类
     */
    private Long catelogId;
    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
     */
    private Integer showDesc;

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
    /**
     * 分组信息
     */
    private Long attrGroupId;

}
