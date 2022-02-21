package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.mapper.ITopOrgMapper;
import com.ll.youthlearn.mapper.IUserMapper;
import com.ll.youthlearn.service.IUserService;
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
@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {

    private ITopOrgMapper topOrgMapper;

    private IUserMapper userMapper;

    private final BCryptPasswordEncoder encoder;


    @Autowired
    public UserServiceImpl(ITopOrgMapper topOrgMapper, IUserMapper userMapper, BCryptPasswordEncoder encoder) {
        this.topOrgMapper = topOrgMapper;
        this.userMapper = userMapper;
        this.encoder = encoder;
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
    }
}
