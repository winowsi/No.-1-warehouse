/**
  * Copyright 2021 json.cn 
  */
package com.winowsi.product.vo.spuvo;

import lombok.Data;

import java.math.BigDecimal;


/**
 * @author Tom
 * @description: 会员价格
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;


}