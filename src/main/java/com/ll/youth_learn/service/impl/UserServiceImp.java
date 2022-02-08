package com.ll.youth_learn.service.impl;

import com.ll.youth_learn.mapper.ITopOrgMapper;
import com.ll.youth_learn.mapper.IUserMapper;
import com.ll.youth_learn.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service("userService")
@Transactional
public class UserServiceImp implements IUserService {

    @Autowired
    private ITopOrgMapper topOrgMapper;

    @Autowired
    private IUserMapper userMapper;

    @Override
    public void selectTopOrgs() throws Exception {

    }

    @Override
    public void selectOrgsByLevels() throws Exception {

    }
}
