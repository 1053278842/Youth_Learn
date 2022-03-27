package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.User;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author: Liu Long
 * @CreateTime: 2022/2/9 0:24
 * @Description:
 */
public interface IUserService {


    /**
     * 插入User,并且也会插入t_user_email表。一对一关联
     * @param user
     * @return
     * @throws Exception
     */
    int insertUser(User user) throws Exception;

}
