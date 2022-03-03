package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.entity.OrgPath;
import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.mapper.IOrgPathMapper;
import com.ll.youthlearn.mapper.ITopOrgMapper;
import com.ll.youthlearn.mapper.IUserMapper;
import com.ll.youthlearn.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author: Liu Long
 * @CreateTime: 2022/2/9 0:31
 * @Description:
 */
@Slf4j
@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {

    private ITopOrgMapper topOrgMapper;
    private IUserMapper userMapper;
    private final IOrgPathMapper orgPathMapper;

    private final BCryptPasswordEncoder encoder;


    @Autowired
    public UserServiceImpl(ITopOrgMapper topOrgMapper, IUserMapper userMapper, BCryptPasswordEncoder encoder, IOrgPathMapper orgPathMapper) {
        this.topOrgMapper = topOrgMapper;
        this.userMapper = userMapper;
        this.encoder = encoder;
        this.orgPathMapper = orgPathMapper;
    }

    @Override
    public void selectTopOrgs() throws Exception {

    }

    @Override
    public void selectOrgsByLevels() throws Exception {

    }

    @Override
    public void insertUser(User user) throws Exception {
        user.setPassword(encoder.encode(user.getPassword()));
        userMapper.insert(user);


//        //maxStage的范围
//        int maxStage= PythonUtils.RegulateNumberRange(35,0,80);
//        //拼接param:xxx|xx->["name1","level2Name"]
//        String orgPathStrParam=PythonUtils.RegulatePath(user.getOrgPath());
//
//        //转换id参数
//        String userStrId=String.valueOf(user.getId());
//        String maxStageStr=String.valueOf(maxStage);
//
//        String pyLocation=location+"GetMemberByPathToMySQL.py";
//        String[] args=new String[]{"python", pyLocation,userStrId,orgPathStrParam,maxStageStr};
//
//        PythonUtils.RunPythonFile(args);


        OrgPath orgPath=new OrgPath();
        orgPath.setUserId(user.getId());
        //TODO 实现user与path表的一对多关系
        orgPath.setOrgPath(user.getOrgPath());
        orgPathMapper.insert(orgPath);
    }
}
