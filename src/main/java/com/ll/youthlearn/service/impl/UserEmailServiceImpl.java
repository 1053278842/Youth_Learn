package com.ll.youthlearn.service.impl;

import com.ll.youthlearn.entity.UserEmail;
import com.ll.youthlearn.mapper.IUserEmailMapper;
import com.ll.youthlearn.service.IUserEmailService;
import lombok.extern.slf4j.Slf4j;
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
@Service()
@Transactional(rollbackFor = Exception.class)
public class UserEmailServiceImpl implements IUserEmailService {

    public final IUserEmailMapper userEmailMapper;

    public UserEmailServiceImpl(IUserEmailMapper userEmailMapper) {
        this.userEmailMapper = userEmailMapper;
    }

    @Override
    public int insertOne(UserEmail userEmail) {
        return userEmailMapper.insert(userEmail);
    }

    @Override
    public int updateOne(UserEmail userEmail) {
        return userEmailMapper.updateById(userEmail);
    }
}
