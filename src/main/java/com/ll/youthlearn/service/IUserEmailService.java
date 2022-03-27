package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.UserEmail;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/24 23:39
 * @Description :
 */
public interface IUserEmailService {

    /**
     * 一般用于创建用户时，自动创建的关联Email信息
     * @param userEmail ps:auto_remind 默认为0
     * @return
     */
    int insertOne(UserEmail userEmail);

    /**
     * 根据传入的对象的ID进行更新，用于用户修改邮件信息
     * @param userEmail
     * @return
     */
    int updateOne(UserEmail userEmail);
}
