package com.winowsi.ware.dao;

import com.winowsi.ware.entity.WareInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库信息
 * 
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-10-15 14:30:39
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {
	
}
