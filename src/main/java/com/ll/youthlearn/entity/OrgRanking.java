package com.ll.youthlearn.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/4/9 0:49
 * @Description : 组织排行表，没有mysql表格。由t_member和t_mes映射关系而成的实体类
 */
@Data
public class OrgRanking implements Serializable {

    private Integer ranking;

    private Integer pathId;

    /**
     * 相当于xx市xx乡xx村中代表的村名
     */
    private String orgLastName;

    /**
     * 组织的人数
     */
    private Integer orgNumSize;

    /**
     * 完成率
     */
    private Double completionRate;

    /**
     * 与某一期相比的排名走势
     * 正数为提升，负数为降低
     */
    private Integer rankingTrend;



}
