package com.ll.youthlearn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.youthlearn.entity.Member;
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
}
