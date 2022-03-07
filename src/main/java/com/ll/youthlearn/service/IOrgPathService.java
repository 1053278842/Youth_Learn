package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.OrgPath;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/1 2:04
 * @Description :
 */

public interface IOrgPathService {
    /**
     * 每当刷新member-table页时，会对t_org_path得到maxMemberNumber列进行更新操作!
     * @param maxNumber 更新的值
     * @param userId 更新条件
     */
    void updateMaxNumberByUserId(Integer maxNumber,Integer userId);

    /**
     * 基础的对象插入
     * @param newOrgPath OrgPath 对象
     */
    void insert(OrgPath newOrgPath);

    /**
     * 根据Id返回list
     * @param id
     * @return
     */
    List<OrgPath> selectListById(Integer id);

    /**
     * 根据ID返回一个
     * @param orgPathId
     * @return
     */
    OrgPath selectOneById(Integer orgPathId);
}
