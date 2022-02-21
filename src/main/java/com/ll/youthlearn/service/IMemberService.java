package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.Member;

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
public interface IMemberService {
    /**
     * 根据当前用户的id，获得所有和当前用户关联的memberList
     * @param userId  当前用户id
     * @param isAsc Ture则升序排列，反之
     * @return  返回与当前用户关联的member列表
     * @throws Exception
     */
    List<Member> selectMemberByUserIdAndOrder(int userId,boolean isAsc) throws Exception;
}
