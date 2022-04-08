package com.ll.youthlearn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.UserEmail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/16 22:08
 * @Description :
 */
@Mapper
public interface IMemberMapper extends BaseMapper<Member> {

    /**
     * 如下所述
     * @return
     * @param userId
     * @param pathId
     * @param isDeleteStatus
     * @param isAsc
     */
    List<Member> selectListContainTimesByUserAndPathAndIsDelete(
            @Param(value = "userId") int userId, @Param(value = "pathId") Integer pathId, @Param(value = "isDelete") int isDeleteStatus,
            @Param(value = "isAsc")boolean isAsc);

    /**
     * 获取指定期次内未学习的同学，并且email not null,
     * 同时auto_remind 即用户User开启了临期次结束时自动提醒按钮的member的email列表
     * @param stageId
     * @return email 列表
     */
    List<UserEmail>  selectEmailAutoRemindLongTime(@Param(value="stageId") int stageId);

    /**
     * 获取指定期次内未学习的同学，并且email not null,
     * 同时auto_remind_start 即用户User开启了当期开始时自动提醒按钮的member的email列表
     * @param stageId
     * @return email 列表
     */
    List<UserEmail>  selectEmailAutoRemindWeekStart(@Param(value="stageId") int stageId);

    /**
     * 根据id筛选未删除的member，将其按照pathId分组，并计算id的count
     * @param uid
     * @return
     */
    List<Member> findCountOfGroupByPath(Integer uid);

    /**
     * 将stage和path分组，统计path的count
     * @param userId
     * @param maxStageNum
     * @param startStageNum
     * @return
     */
    List<Member> findPathAvgOfGroupCount(@Param("userId")Integer userId, @Param("startStageNum")Integer startStageNum, @Param("maxStageNum")Integer maxStageNum);
}
