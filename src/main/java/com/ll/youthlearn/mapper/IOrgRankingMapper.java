package com.ll.youthlearn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.youthlearn.entity.Org;
import com.ll.youthlearn.entity.OrgRanking;
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
 * @CreateTime :  2022/2/9 1:29
 * @Description :
 */
@Mapper
public interface IOrgRankingMapper extends BaseMapper<Org> {

    /**
     * 返回按照期次分组后有关每个组织路径下学习人数总计的排名对象列表
     * @param uid 用户Id
     * @param stageTotal 返回多少期的数据
     * @return
     */
    List<OrgRanking> selectTotalNumberOfLearnersGroupedByStage(
            @Param(value = "uid") Integer uid,
            @Param(value = "stageTotal") Integer stageTotal);
}
