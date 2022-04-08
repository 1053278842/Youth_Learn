package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.UserEmail;

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
     * 根据当前用户的id，获得所有和当前用户关联的memberList，isDelete=0
     * @param userId  当前用户id
     * @param pathId
     * @param isAsc Ture则升序排列，反之
     * @return  返回与当前用户关联的member列表
     * @throws Exception
     */
    List<Member> selectMemberByUserIdAndPathId(int userId, Integer pathId, boolean isAsc) throws Exception;

    /**
     * 根据当前用户的id，获得所有和当前用户关联的memberList，相对于selectMemberByUserIdAndPathId，该方法可以指定isDelete
     * @param userId
     * @param pathId
     * @param isAsc
     * @param i
     * @return
     */
    List<Member> selectMemberByUserIdAndPathIdAndIsDelete(int userId, Integer pathId, boolean isAsc, int i);

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
     * @throws Exception
     */
    int deleteOneWithId(Integer memberId) throws Exception;

    /**
     * 根据用户手动传入的name和email插入新数据
     * 时间为当前时间，id自增，time=0....
     * @param memberName
     * @param memberEmail
     * @param parentId
     * @param path
     * @return
     * @throws Exception
     */
    int addMemberByNameAndEmail(String memberName, String memberEmail,Integer parentId,String path) throws Exception;

    /**
     * 根据pathID返回多条结果,注意。该方法会会忽略isDelete的状态，返回全部的pathId的member
     * @param pathId
     * @return
     */
    List<Member> selectByPathIdIgnoreIsDelete(Integer pathId);

    /**
     * 插入一个Member
     * @param newMember
     * @return
     */
    int insertOne(Member newMember);

    /**
     * 根据memberId恢复指定的member，set isDelete=0
     * @param memberId
     * @param memberId
     * @return 返回删除结果 int
     * @throws Exception
     */
    int resumeOneWithId(Integer memberId);

    /**
     * 根据Id返回Member
     * @param memberId
     * @return
     */
    Member selectMemberById(int memberId);

    /**
     * 获取指定期次内未学习的同学，并且email not null,
     * 同时auto_remind 即用户User开启了临期次结束时自动提醒按钮的member的email列表
     * @param stageId
     * @return email 列表
     */
    List<UserEmail>  selectEmailAutoRemindLongTime(int stageId);

    /**
     * 获取指定期次内未学习的同学，并且email not null,
     * 同时auto_remind_start 即用户User开启了当期开始时自动提醒按钮的member的email列表
     * @param stageId
     * @return email 列表
     */
    List<UserEmail>  selectEmailAutoRemindWeekStart(int stageId);

    /**
     * name为条件模糊搜索member
     *
     * @param uid
     * @param fuzzy_name 模糊搜索的name
     * @return 返回0-n个数据
     */
    List<Member> searchListByFuzzyName(Integer uid, String fuzzy_name);

    /**
     * 单用户下，根据不同路径组织分组，计算获得每个组织下的【未删除成员总数】
     * @param uid
     * @return
     */

    List<Member> findCountOfGroupByPathId(Integer uid);
    /**
     * 获取mes列表，包含不同期次下不同路径分组后的人数平均数。
     * 用于分析单个用户下，指定期次数量中，同路径组织的学习人数（mes.member.countId）的平均数
     * @param userId 用户id
     * @param startStageNum 相当于limit 0
     * @param maxStageNum 从最近的期次开始向下取X个期次
     * @return
     */
    List<Member> findAvgOfGroupByPathCount(Integer userId, Integer startStageNum,Integer maxStageNum);
}
