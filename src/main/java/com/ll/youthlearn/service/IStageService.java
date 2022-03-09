package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.Stage;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/25 16:19
 * @Description :
 */
public interface IStageService {
    /**
     * select * from t_stage right join t_member_each_stage on t_stage.id=stage_id;
     * 根据用户 的userId查找到stage...
     * @param userId ,.
     * @param pathId
     * @return ad
     */
    List<Stage> findStagesByUserId(int userId,Integer pathId);

    /**
     * 根据传入的当前时间范围，获取最新的Stage对象
     * @return
     */
    Stage findNewestStage();
}
