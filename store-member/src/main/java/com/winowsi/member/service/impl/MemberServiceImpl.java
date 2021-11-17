package com.winowsi.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.winowsi.member.exception.PhoneExsitException;
import com.winowsi.member.exception.UserNameExsitException;
import com.winowsi.member.utils.HttpUtils;
import com.winowsi.member.vo.MemberLoginVo;
import com.winowsi.member.vo.MemberRegisterVo;
import com.winowsi.member.vo.TokenVo;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winowsi.common.utils.PageUtils;
import com.winowsi.common.utils.Query;

import com.winowsi.member.dao.MemberDao;
import com.winowsi.member.entity.MemberEntity;
import com.winowsi.member.service.MemberService;


/**
 * @author Tom
 */
@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    private final MemberDao memberDao;
    MemberServiceImpl(MemberDao memberDao){
        this.memberDao=memberDao;
    }
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegisterVo memberRegisterVo) {
        MemberEntity member=new MemberEntity();
        //查到默认等级
        MemberEntity memberEntity=memberDao.getDefaultLevel();
        member.setLevelId(memberEntity.getId());
        //检查用户是否存在 存在就抛出异常
        checkPhoneUnique(memberRegisterVo.getPhone());
        checkUserNameUnique(memberRegisterVo.getUserName());

        member.setUsername(memberRegisterVo.getUserName());
        member.setNickname(memberRegisterVo.getUserName());
        member.setMobile(memberRegisterVo.getPhone());
        //保存加密后的密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(memberRegisterVo.getPassword());
        member.setPassword(encode);

        baseMapper.insert(member);

    }

    @Override
    public void checkPhoneUnique(String phone)throws PhoneExsitException{
        Integer count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (count>0){
            throw  new  PhoneExsitException();
        }
    }

    @Override
    public void checkUserNameUnique(String userName)throws UserNameExsitException {
        Integer count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if (count>0){
            throw   new UserNameExsitException();
        }

    }

    @Override
    public MemberEntity login(MemberLoginVo memberLoginVo) {
        String accountUserName = memberLoginVo.getAccountUserName();
        MemberEntity member = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", accountUserName).or().eq("mobile", accountUserName));
        if (member==null){
            return null;
        }else {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            boolean matches = bCryptPasswordEncoder.matches(memberLoginVo.getPassword(), member.getPassword());
            if (matches){
                return member;
            } else{
                return  null;
            }
        }


    }

    @Override
    public MemberEntity login(TokenVo tokenVo)  {
        MemberDao baseMapper = this.baseMapper;

        MemberEntity member = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("uid", tokenVo.getUid()));
        if (null!=member){
            MemberEntity mens = new MemberEntity();
            mens.setId(member.getId());
            mens.setAccessToken(tokenVo.getAccess_token());
            mens.setExpiresIn(tokenVo.getExpires_in());
            //更新数据
            baseMapper.updateById(mens);
            //返回数据
            member.setAccessToken(tokenVo.getAccess_token());
            member.setExpiresIn(tokenVo.getExpires_in());
            return member;
        }else {
            MemberEntity mens = new MemberEntity();
            Map<String,String> query=new HashMap<>();
            query.put("uid",tokenVo.getUid());
            query.put("access_token",tokenVo.getAccess_token());

            JSONObject jsonObject = null;
            try {
                HttpResponse weiBoData = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "get", new HashMap<>(), query);
                String s = EntityUtils.toString(weiBoData.getEntity());
                jsonObject = JSON.parseObject(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mens.setHeader(jsonObject.getString("profile_image_url"));
            mens.setNickname(jsonObject.getString("screen_name"));
            mens.setSign(jsonObject.getString("description"));
            mens.setGender("m".equals(jsonObject.getString("gender"))?1:0);
            mens.setCreateTime(new Date());
            mens.setUid(tokenVo.getUid());
            mens.setAccessToken(tokenVo.getAccess_token());
            mens.setExpiresIn(tokenVo.getExpires_in());
            //注册
            memberDao.insert(mens);
            return mens;
        }
    }


}