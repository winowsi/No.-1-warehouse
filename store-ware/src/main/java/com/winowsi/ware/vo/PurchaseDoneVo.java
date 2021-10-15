package com.winowsi.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/15 17:08
 */
@Data
public class PurchaseDoneVo {

    /**
     * 采购单id
     */
    @NotNull
    private  Long id;

    private List<ItemDoneVo> items;

}
