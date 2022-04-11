package com.ll.youthlearn.service;

import com.ll.youthlearn.entity.OrgRanking;

import java.util.List;

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
public interface IOrgRankingService {


    /**
     * 返回当前期次的排名数据
     * @param uid 用户id
     * @return
     */
    List<OrgRanking> findCurrentStageRanking(Integer uid) ;

    /**
     * 范围查找，可指定最新期次开始到endNums个期次的范围内排名
     * @param uid
     * @param startIndex 相当于 limit x,y 的 x
     * @param endNums 相当于 limit x,y 中的 y
     * @return
     */
    List<OrgRanking> findRangeStageRanking(Integer uid,Integer startIndex,Integer endNums);

    /**
     * 返回类似：
     * ['product'，'第7期','第6期',‘第5期’,‘第4期’]
     * ['19信管'，   88,    32,    43,     22]
     * ['21计科'，   88,    32,    43,     22]
     * 提供Echarts的Line型必要数据。
     * 其中数据为达成率（百分制）
     * @param uid
     * @param stageTotal
     * @return
     */
    List<Object[]> getEchartsLineRankingDate(Integer uid,Integer stageTotal);
}
