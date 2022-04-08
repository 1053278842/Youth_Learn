package com.ll.youthlearn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.youthlearn.entity.MemberEachStage;
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
 * @CreateTime :  2022/2/25 15:41
 * @Description :
 */
@Mapper
public interface IMemberEachStageMapper extends BaseMapper<MemberEachStage> {
    /**
     * 删除
     * @param userId rt
     * @param maxStageNumber rt
     * @param pathId
     */
    void deleteByUserIdAndMaxStageNumber(@Param("userId") Integer userId,@Param(value = "pathId") Integer pathId, @Param("maxStageNumber") Integer maxStageNumber);

    /**
     * 批量插入列表
     * @param mesList MemberEachStage对象列表
     */
    void insertMany(List<MemberEachStage> mesList);

    /**
     * 根据单组织的三维限制id,检索出单个用户、单个组织路径下、单个期次下的成员情况
     * @param stageId 期次id
     * @param userId 用户id
     * @param orgPathId 组织路径id
     * @return  List<MemberEachStage> + OneToOne关系下Member
     */
    List<MemberEachStage> selectMesContainRelationshipByOneOrgParam(
            @Param("stageId") int stageId, @Param("userId") Integer userId,
            @Param("pathId") Integer orgPathId
    );

    /**
     * 左连接stage,orderBy stage的时间。升序
     * @param memberId
     * @return
     */
    List<MemberEachStage> selectListByMemberId(Integer memberId);


}
