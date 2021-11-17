package com.winowsi.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.member.entity.MemberEntity;
import com.winowsi.member.exception.PhoneExsitException;
import com.winowsi.member.exception.UserNameExsitException;
import com.winowsi.member.vo.MemberLoginVo;
import com.winowsi.member.vo.MemberRegisterVo;
import com.winowsi.member.vo.TokenVo;

import java.util.Map;

/**
 * 会员
 *
 * @author zhaoyao
 * @email winowsi@outlook.com
 * @date 2021-10-13 10:12:43
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVo memberRegisterVo);

    /**
     * 电话是否唯一
     * @param  phone 电话
     * @return true/false
     */
    void checkPhoneUnique(String phone)throws PhoneExsitException;

    /**
     * 用户民是否唯一
     * @param userName 用户名
     * @return true/false
     */
    void checkUserNameUnique(String userName)throws UserNameExsitException;

    MemberEntity login(MemberLoginVo memberLoginVo);

    MemberEntity login(TokenVo tokenVo) ;
}

