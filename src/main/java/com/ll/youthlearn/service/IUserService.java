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

    void selectTopOrgs() throws Exception;

    void selectOrgsByLevels() throws Exception;

    void insertUser(User user) throws Exception;

//    void insertOrg()
}
