package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.entity.Stage;
import com.ll.youthlearn.mapper.IStageMapper;
import com.ll.youthlearn.service.IStageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Stage> findStagesByUserId(int userId) {
        return stageMapper.selectStageListByUserId(userId);
    }
}
