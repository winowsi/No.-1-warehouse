
package com.winowsi.product.vo.spuvo;

import lombok.Data;

/**
 *
 * @author Tom
 */
@Data
public class BaseAttrs {

    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性默认值
     */
    private String attrValues;
    /**
     * 快速显示
     */
    private int showDesc;

}