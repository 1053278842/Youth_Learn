package com.ll.youthlearn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.youthlearn.entity.MemberEachStage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     */
    void deleteByUserIdAndMaxStageNumber( @Param("userId") Integer userId, @Param("maxStageNumber") Integer maxStageNumber);
}
