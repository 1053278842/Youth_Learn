package com.ll.youthlearn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.UserEmail;
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
    public List<Member> selectMemberByUserIdAndPathId(int userId, Integer pathId, boolean isAsc) throws Exception {

        return memberMapper.selectListContainTimesByUserAndPathAndIsDelete(userId,pathId,0,isAsc);
    }

    @Override
    public List<Member> selectMemberByUserIdAndPathIdAndIsDelete(int userId, Integer pathId, boolean isAsc, int i) {
        return memberMapper.selectListContainTimesByUserAndPathAndIsDelete(userId,pathId,i,isAsc);
    }

    @Override
    public int updateOneWithId(Integer id, String email) throws Exception {
        Member tempM=new Member();
        tempM.setId(id);
        tempM.setEmail(email);
        return memberMapper.updateById(tempM);
    }

    @Override
    public int deleteOneWithId(Integer memberId) throws Exception {
        Member tempM=new Member();
        tempM.setId(memberId);
        tempM.setIsDelete(1);
        return memberMapper.updateById(tempM);
    }

    @Override
    public int addMemberByNameAndEmail(String memberName, String memberEmail,Integer parentId,String path) {
        Member member=new Member();
        member.setEmail(memberEmail);
        member.setName(memberName);
        member.setPath(path);
        member.setParentUserId(parentId);
        return memberMapper.insert(member);
    }

    @Override
    public List<Member> selectByPathIdIgnoreIsDelete(Integer pathId) {
        return memberMapper.selectList(new QueryWrapper<Member>().eq("path_id",pathId));
    }

    @Override
    public int insertOne(Member newMember) {
        return memberMapper.insert(newMember);
    }

    @Override
    public int resumeOneWithId(Integer memberId) {
        Member tempM=new Member();
        tempM.setId(memberId);
        tempM.setIsDelete(0);
        return memberMapper.updateById(tempM);
    }

    @Override
    public Member selectMemberById(int memberId) {
        return memberMapper.selectById(memberId);
    }

    @Override
    public List<UserEmail>  selectEmailAutoRemindLongTime(int stageId) {
        return memberMapper.selectEmailAutoRemindLongTime(stageId);
    }

    @Override
    public List<UserEmail> selectEmailAutoRemindWeekStart(int stageId) {
        return memberMapper.selectEmailAutoRemindWeekStart(stageId);
    }

    @Override
    public List<Member> searchListByFuzzyName(Integer uid, String fuzzy_name) {
        return memberMapper.selectList(
                new QueryWrapper<Member>()
                .eq("parent_user_id",uid)
                .eq("isDelete",0)
                .like("name",fuzzy_name)
        );
    }

    @Override
    public List<Member> findCountOfGroupByPathId(Integer uid) {
        return memberMapper.findCountOfGroupByPath(uid);
    }
}
