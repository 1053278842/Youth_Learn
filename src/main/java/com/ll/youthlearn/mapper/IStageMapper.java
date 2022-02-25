package com.ll.youthlearn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.youthlearn.entity.Stage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/25 13:04
 * @Description :
 */
@Mapper
public interface IStageMapper extends BaseMapper<Stage> {
    /**
     * 获取当前用户管理下的所有Stage；其中还包含了每个stage的MemberList，使用懒加载
     * @param userId 当前用户的id
     * @return 返回Stage列表
     */
    List<Stage> selectStageListByUserId(int userId);
}
