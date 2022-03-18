package com.ll.youthlearn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ll.youthlearn.entity.MemberEachStage;
import com.ll.youthlearn.entity.Org;
import com.ll.youthlearn.entity.OrgPath;
import com.ll.youthlearn.mapper.IOrgPathMapper;
import com.ll.youthlearn.service.IOrgPathService;
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
@Service("orgPathService")
@Transactional(rollbackFor = Exception.class)
public class OrgPathServiceImpl implements IOrgPathService {

    private final IOrgPathMapper orgPathMapper;

    public OrgPathServiceImpl(IOrgPathMapper orgPathMapper) {
        this.orgPathMapper = orgPathMapper;
    }


    @Override
    public void updateMaxNumberByPathId(Integer maxNumber, Integer pathId) {
        orgPathMapper.update(null,new UpdateWrapper<OrgPath>().eq("id",pathId).set("max_member_number",maxNumber));
    }

    @Override
    public void insert(OrgPath newOrgPath) {
        orgPathMapper.insert(newOrgPath);
    }

    @Override
    public List<OrgPath> selectListById(Integer id) {
        return orgPathMapper.selectList(new QueryWrapper<OrgPath>().eq("user_id",id).orderByAsc("org_path"));
    }

    @Override
    public OrgPath selectOneById(Integer orgPathId) {
        return orgPathMapper.selectOne(new QueryWrapper<OrgPath>().eq("id",orgPathId));
    }

    @Override
    public void delByPathId(Integer pathId) {
        orgPathMapper.deleteById(pathId);
    }

    @Override
    public List<OrgPath> selectList() {
        return orgPathMapper.selectList(null);
    }

    @Override
    public List<OrgPath> selectListByOrgPathStr(String currentOrgPathStr) {
        return orgPathMapper.selectList(new QueryWrapper<OrgPath>().eq("org_path",currentOrgPathStr));
    }
}
