package com.ll.youthlearn.factory;

import com.ll.youthlearn.entity.Org;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/2/24 23:26
 * @Description :
 */
public interface IPythonSpider {

    public final String location=System.getProperty("user.dir") + "\\src\\main\\java\\com\\ll\\youthlearn\\python\\";

    /**
     * 查找所有的stage，然后持久化
     */
    void saveAllStage();

    /**
     * 爬取Member数据，业务上体现为为一个班级检索其所有相关人员。做去重处理！
     * @param id    用户即user对象的id属性
     * @param orgPath   user对象的orgPath，格式为"xxx|xxx"
     * @param maxStage  查找范围，指定了从当前期开始，持续X期的数据，注意有0-80的范围限制！
     */
    void saveMemberOfOnePath(int id,String orgPath,int maxStage);

    /**
     * 爬取Member数据，业务上体现为检索指定maxStage期内的所有人员信息，不做去重处理！
     * @param id    用户即user对象的id属性
     * @param orgPath   user对象的orgPath，格式为"xxx|xxx"
     * @param maxStage  查找范围，指定了从当前期开始，持续X期的数据，注意有0-80的范围限制！
     */
    void saveMemberEachStage(int id,String orgPath,int maxStage);

    /**
     * 爬取Org数据，根据固定格式传入的orgs进行35期范围内的org爬取。主要业务是创建账号时所用的联动查询！
     * @param orgs org列表 [org1,org2]；正常情况下是按照级别顺序排列的path，如[org1:安徽,org2:六安]
     * @return 返回org级下的35期内曾出现过的组织列表。以List<org>每个org的Name属性中。
     */
    List<Org> getOrgJsonByName(Org[] orgs);

    /**
     * 检查是否符合路径的要求
     * @param orgPath org列表 [org1,org2]；正常情况下是按照级别顺序排列的path，如[org1:安徽,org2:六安]
     * @return Ture/False
     */
    boolean checkOrgPathAvailable(String orgPath);
}
