package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.MemberEachStage;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/1 18:00
 * @Description :
 */
public interface IMemberEachStageService {
    /**
     * 删除若干memberEachStage，根据maxStage的大小
     * @param userId
     * @param maxStage
     */
    void deleteMemberEachStageByUserIdAndMaxStage(Integer userId,Integer maxStage);

    /**
     * 批量插入列表对象，单次提交
     * @param mesList memberEachStage对象列表
     */
    void insertMany(List<MemberEachStage> mesList);

    /**
     * 根据单组织的三维限制id,检索出单个用户、单个组织路径下、单个期次下的成员情况
     * @param stageId 期次id
     * @param userId 用户id
     * @param orgPathId 组织路径id
     * @return  List<MemberEachStage> + OneToOne关系下Member
     */
    List<MemberEachStage> selectByOneOrg(int stageId, Integer userId, Integer orgPathId);


}
