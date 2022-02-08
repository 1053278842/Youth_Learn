package com.ll.youth_learn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.youth_learn.entity.TopOrg;
import com.ll.youth_learn.mapper.IOrgMapper;
import com.ll.youth_learn.mapper.ITopOrgMapper;
import com.ll.youth_learn.service.IOrgService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @CreateTime :  2022/2/9 1:31
 * @Description :
 */
@Transactional
@Service("orgService")
public class OrgServiceImpl implements IOrgService {

    @Autowired
    private IOrgMapper orgMapper;

    @Autowired
    private ITopOrgMapper topOrgMapper;

    @Override
    public List<TopOrg> selectTopOrgList() throws Exception {
        return topOrgMapper.selectList(new QueryWrapper<TopOrg>().orderByDesc("id"));
    }
}
