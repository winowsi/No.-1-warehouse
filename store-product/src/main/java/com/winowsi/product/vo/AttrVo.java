package com.winowsi.product.vo;


import com.winowsi.product.entity.AttrEntity;
import lombok.Data;

/**
 * @description: AttrVo请求数据
 * @Author:ZaoYao
 * @Time: 2021/10/11 11:40
 */
@Data
public class AttrVo extends AttrEntity {

    /**
     * 分组信息
     */
    private Long attrGroupId;
}
