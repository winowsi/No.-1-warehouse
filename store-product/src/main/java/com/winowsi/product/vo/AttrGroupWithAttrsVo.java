package com.winowsi.product.vo;

import com.winowsi.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/13 15:52
 */
@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 所在分组的所有属性
     */
    private List<AttrEntity> attrs;
}
