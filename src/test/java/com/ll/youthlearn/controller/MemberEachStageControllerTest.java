package com.ll.youthlearn.controller;

import com.alibaba.fastjson.JSONArray;
import com.ll.youthlearn.entity.Member;
import com.ll.youthlearn.entity.OrgPath;
import com.ll.youthlearn.entity.Stage;
import com.ll.youthlearn.service.IMemberService;
import com.ll.youthlearn.service.IOrgPathService;
import com.ll.youthlearn.service.IStageService;
import com.ll.youthlearn.utils.JsonRegularUtils;
import com.ll.youthlearn.utils.SpiderUtils;
import com.ll.youthlearn.utils.UrlRegularUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/3/9 13:20
 * @Description :
 */
@WebAppConfiguration
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class MemberEachStageControllerTest {

    @Autowired
    private IStageService stageService;
    @Autowired
    private IOrgPathService orgPathService;
    @Autowired
    private IMemberService memberService;
    @Test
    void getCurrentStageMember() throws IOException {

        //判断当前时间下，是否存在最新的期次，即是否属于寒暑假放假期间
        Stage stage=stageService.findNewestStage();
        if(stage!=null){

            //获取org_top列
            List<OrgPath> orgPathList=orgPathService.selectList();

            //去除orgPath相同的记录,防止重复爬取,造成性能损失
            HashMap<String,OrgPath> hashMap=new HashMap(orgPathList.size());
            for (OrgPath op: orgPathList) {
                hashMap.put(op.getOrgPath(),op);
            }
            List<OrgPath> orgPathListNoRepeat=new ArrayList<OrgPath>();
            for (OrgPath op: hashMap.values()) {
                orgPathListNoRepeat.add(op);
            }

            //根据OrgPath列表进行爬虫抓取,抓到member_each_stage表中
            //TODO 使用多线程
            for (OrgPath op : orgPathListNoRepeat ) {
                String url="dxx.ahyouth.org.cn/api/peopleRankStage";
                url = UrlRegularUtils.setRequestProtocolHeader(url,false);
                url = UrlRegularUtils.setStage(url,stage.getStage());
                url = UrlRegularUtils.setLevelParams(url,op.getOrgPath());

                //插入和检索需要的主键Id
                Integer stageId = stage.getId();
                Integer userId = op.getUserId();
                Integer pathId = op.getId();

                String jsonData = SpiderUtils.getHttpJson(url);
                if(jsonData!=null){
                    //转换成json数据处理
                    JSONArray jsonArray= JsonRegularUtils.getMemberListByJsonData(jsonData);
                    //打印
                    System.out.println(MessageFormat.format("当前组织：{0},当前期次：{1},爬取到成员数量：{2}",
                            op.getOrgPath(),stage.getName(),jsonArray.size()));

                    //TODO 对新用户进行add操作

                    //获取memberList,作为限制的依据
                    List<Member> memberList = memberService.selectByPathId(pathId);
                    HashMap<String,Member> memberHashMap=new HashMap<>();
                    for (Member member:memberList) {
                        memberHashMap.put(member.getName(),member);
                    }

                    //插入
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Member member = memberHashMap.get(jsonArray.getJSONObject(i));
                        //json数据中的用户不在member表中的情况
                        if(member==null){
                            continue;
                        }

                        Integer memberId=memberHashMap.get(jsonArray.getJSONObject(i).get("username")).getId();
                    }
                }
            }
        }
    }
}