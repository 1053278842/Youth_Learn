package com.ll.youthlearn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ll.youthlearn.entity.TopOrg;
import com.ll.youthlearn.mapper.IOrgMapper;
import com.ll.youthlearn.mapper.ITopOrgMapper;
import com.ll.youthlearn.service.IOrgService;
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
@Transactional(rollbackFor = Exception.class)
@Service("orgService")
public class OrgServiceImpl implements IOrgService {

    private final IOrgMapper orgMapper;

    private final ITopOrgMapper topOrgMapper;

    public OrgServiceImpl(IOrgMapper orgMapper, ITopOrgMapper topOrgMapper) {
        this.orgMapper = orgMapper;
        this.topOrgMapper = topOrgMapper;
    }

    @Override
    public List<TopOrg> selectTopOrgList() throws Exception {
        return topOrgMapper.selectList(new QueryWrapper<TopOrg>().orderByDesc("id"));
    }
}
