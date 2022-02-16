package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.TopOrg;

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
     * 返回所有的TopOrg ，按照id降序排列(-1,-2...-10)
     * @return
     * @throws Exception
     */
    List<TopOrg> selectTopOrgList() throws Exception;
}
