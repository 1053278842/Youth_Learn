package com.ll.youthlearn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.youthlearn.entity.MemberEachStage;
import org.apache.ibatis.annotations.Mapper;

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
//    /**
//     * 根据ID查找member
//     * @param stage_id 对应stage表中的主键id
//     * @return 返回单个member对象
//     */
//    @Select("SELECT id,name,stage FROM t_stage")
//    Member selectOneByStageId(int stage_id);
}
