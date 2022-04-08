package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.entity.MemberEachStage;
import com.ll.youthlearn.mapper.IMemberEachStageMapper;
import com.ll.youthlearn.service.IMemberEachStageService;
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
 * @CreateTime :  2022/3/1 18:03
 * @Description :
 */
@Service("memberEachStageService")
@Transactional(rollbackFor=Exception.class)
public class MemberEachStageServiceImpl implements IMemberEachStageService {

    private final IMemberEachStageMapper memberEachStageMapper;

    public MemberEachStageServiceImpl(IMemberEachStageMapper memberEachStageMapper) {
        this.memberEachStageMapper = memberEachStageMapper;
    }

    @Override
    public void deleteMemberEachStageByUserIdAndMaxStage(Integer userId,Integer pathId, Integer maxStage) {
        memberEachStageMapper.deleteByUserIdAndMaxStageNumber(userId,pathId,maxStage);
    }

    @Override
    public void insertMany(List<MemberEachStage> mesList) {
        memberEachStageMapper.insertMany(mesList);
    }

    @Override
    public List<MemberEachStage> selectByOneOrg(int stageId, Integer userId, Integer orgPathId) {
        return memberEachStageMapper.selectMesContainRelationshipByOneOrgParam(stageId, userId, orgPathId);
    }

    @Override
    public List<MemberEachStage> selectListByMemberId(Integer memberId) {
        return memberEachStageMapper.selectListByMemberId(memberId);
    }

    @Override
    public List<MemberEachStage> findCountOfGroupByPath(Integer userId, Integer maxStageNum) {
        if(maxStageNum<0){maxStageNum=0;}
        return memberEachStageMapper.FindPathCountOfGroup(userId,maxStageNum);
    }

}
