package com.winowsi.member.dao;

import com.winowsi.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-10-13 10:12:43
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {

    MemberEntity getDefaultLevel();
}
