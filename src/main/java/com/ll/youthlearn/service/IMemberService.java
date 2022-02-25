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

    /**
     *  根据传入的id修改对应member的email字段值
     * @param id
     * @param email
     * @return
     * @throws Exception
     */
    int updateOneWithId(Integer id,String email) throws Exception;

    /**
     * 根据memberId删除指定的member
     * @param memberId
     * @param memberId
     * @return 返回删除结果 int
     */
    int deleteOneWithId(Integer memberId) throws Exception;

    /**
     * 根据用户手动传入的name和email插入新数据
     * 时间为当前时间，id自增，time=0....
     * @param memberName
     * @param memberEmail
     * @param parentId
     * @param maxTimes
     * @param path
     * @return
     */
    int addMemberByNameAndEmail(String memberName, String memberEmail,Integer maxTimes,Integer parentId,String path) throws Exception;
}
