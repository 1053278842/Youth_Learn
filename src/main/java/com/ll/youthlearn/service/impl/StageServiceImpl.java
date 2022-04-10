package com.ll.youthlearn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.youthlearn.entity.Stage;
import com.ll.youthlearn.mapper.IStageMapper;
import com.ll.youthlearn.service.IStageService;
import com.ll.youthlearn.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/25 16:23
 * @Description :
 */
@Service("stageService")
@Transactional(rollbackFor = Exception.class)
public class StageServiceImpl implements IStageService {

    private final IStageMapper stageMapper;

    public StageServiceImpl(IStageMapper stageMapper) {
        this.stageMapper = stageMapper;
    }

    @Override
    public List<Stage> findStagesByUserId(int userId, Integer pathId) {
        return stageMapper.selectStageListByUserId(userId,pathId);
    }

    @Override
    public Stage findNewestStage() {

        Timestamp currentTs = new Timestamp(System.currentTimeMillis());

        String MonTs;
        String SunTs;


        MonTs= DateUtils.getWeekMondayDate("yyyyMMdd",currentTs);
        SunTs = DateUtils.getWeekSundayDate("yyyMMdd",currentTs);

        return stageMapper.selectOne(new QueryWrapper<Stage>().between("stage_date",MonTs,SunTs).orderByDesc("stage").last("limit 0,1"));
    }

    @Override
    public List<Stage> findNewStageByNum(Integer num) {

        return stageMapper.selectList(new QueryWrapper<Stage>()
                .orderByDesc("stage_date")
                .last("limit 0,"+num));
    }

}
