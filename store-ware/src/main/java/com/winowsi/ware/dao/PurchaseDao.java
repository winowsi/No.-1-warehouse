package com.winowsi.ware.dao;

import com.winowsi.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-10-15 14:30:39
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
