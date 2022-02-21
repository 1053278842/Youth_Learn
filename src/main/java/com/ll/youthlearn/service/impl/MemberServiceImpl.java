package com.ll.youthlearn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.mapper.IMemberMapper;
import com.ll.youthlearn.service.IMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/16 22:09
 * @Description :
 */
@Transactional(rollbackFor=Exception.class)
@Service("memberService")
@Slf4j
public class MemberServiceImpl implements IMemberService{

    private final IMemberMapper memberMapper;

    public MemberServiceImpl(IMemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }


    @Override
    public List<Member> selectMemberByUserIdAndOrder(int userId,boolean isAsc) throws Exception {
        if(isAsc){
            return memberMapper.selectList(new QueryWrapper<Member>().eq("parent_user_id",userId).orderByAsc("times"));
        }else{
            return memberMapper.selectList(new QueryWrapper<Member>().eq("parent_user_id",userId).orderByDesc("times"));
        }
    }
}
