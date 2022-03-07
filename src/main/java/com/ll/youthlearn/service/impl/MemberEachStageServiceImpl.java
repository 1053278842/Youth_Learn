package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.mapper.IMemberEachStageMapper;
import com.ll.youthlearn.service.IMemberEachStageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void deleteMemberEachStageByUserIdAndMaxStage(Integer userId, Integer maxStage) {
        memberEachStageMapper.deleteByUserIdAndMaxStageNumber(userId,maxStage);
    }
}
