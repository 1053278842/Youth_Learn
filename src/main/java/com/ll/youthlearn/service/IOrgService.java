package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.Org;
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

    /**
     * 根据name获取一个Org对象
     * @param parentStr name
     * @return Org对象
     */
    Org selectOneByName(String parentStr);

    /**
     * 批量插入Org
     * @param results_orgs List<Org>
     */
    void insertManyOrg(List<Org> results_orgs)throws Exception;
}
