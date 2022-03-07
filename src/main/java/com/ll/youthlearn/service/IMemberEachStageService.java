package com.ll.youthlearn.service;

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
}
