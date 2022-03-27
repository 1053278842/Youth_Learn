package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.entity.User;
import com.ll.youthlearn.entity.UserEmail;
import com.ll.youthlearn.mapper.IOrgPathMapper;
import com.ll.youthlearn.mapper.ITopOrgMapper;
import com.ll.youthlearn.mapper.IUserEmailMapper;
import com.ll.youthlearn.mapper.IUserMapper;
import com.ll.youthlearn.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final IUserEmailMapper userEmailMapper;

    private final BCryptPasswordEncoder encoder;

    @Value("${custom-chart.user-email.content}")
    private String defaultEmailContent;
    @Value("${custom-chart.user-email.title}")
    private String defaultTitle;

    @Autowired
    public UserServiceImpl(ITopOrgMapper topOrgMapper, IUserMapper userMapper, BCryptPasswordEncoder encoder, IOrgPathMapper orgPathMapper, IUserEmailMapper userEmailMapper) {
        this.topOrgMapper = topOrgMapper;
        this.userMapper = userMapper;
        this.encoder = encoder;
        this.orgPathMapper = orgPathMapper;
        this.userEmailMapper = userEmailMapper;
    }

    @Override
    public int insertUser(User user) throws Exception {
        //用户密码需要加密
        user.setPassword(encoder.encode(user.getPassword()));
        int insertStatus=userMapper.insert(user);
        if (insertStatus==0){
            log.warn("用户创建失败！未能插入用户数据");
            return 0;
        }

        //同时创建email表中的信息,并做外键关联
        UserEmail userEmail = new UserEmail();
        userEmail.setUserId(user.getId());
        userEmail.setName(user.getUsername());
        userEmail.setEmail(user.getEmail());
        userEmail.setTitle(defaultTitle);
        //默认0,保险起见还是输入
        userEmail.setAutoRemind(0);
        userEmail.setContent(defaultEmailContent);
        insertStatus=userEmailMapper.insert(userEmail);
        if (insertStatus==0){
            log.warn("用户创建失败！用户创建已完成,但是未能插入相关Email数据.");
            return 0;
        }
        return 1;
    }

}
