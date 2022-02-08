package com.ll.youth_learn.service;

import com.ll.youth_learn.entity.TopOrg;

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
public interface IOrgService {
    /**
     * 返回所有的TopOrg
     * @return
     * @throws Exception
     */
    List<TopOrg> selectTopOrgList() throws Exception;
}
